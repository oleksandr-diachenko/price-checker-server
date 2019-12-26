package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import oleksandrdiachenko.pricechecker.util.WorkbookUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Alexander Diachenko
 */
@Service
public class QueueService {

    private PriceCheckService priceCheckService;
    private FileRepository fileRepository;
    private FileStatusRepository fileStatusRepository;

    private Queue<PriceCheckParameter> queue = new ConcurrentLinkedQueue<>();

    @Autowired
    public QueueService(PriceCheckService priceCheckService, FileRepository fileRepository,
                        FileStatusRepository fileStatusRepository) {
        this.priceCheckService = priceCheckService;
        this.fileRepository = fileRepository;
        this.fileStatusRepository = fileStatusRepository;
    }

    public void start(PriceCheckParameter parameter) {
        queue.add(parameter);
        createNewRecord(parameter);
        while (!queue.isEmpty()) {
            Runnable myRunnable = () -> {
                PriceCheckParameter poll = queue.poll();
                if (poll != null) {
                    FileStatus fileStatus = fileStatusRepository.findByName(poll.getName());
                    fileStatus.setStatus(Status.PENDING.name());
                    fileStatusRepository.save(fileStatus);

                    Workbook workbook = buildWorkbook(poll, fileStatus);

                    File file = fileRepository.findByFileStatus(fileStatus);
                    byte[] bytes = getBytesFromWorkbook(workbook, fileStatus);
                    file.setFile(bytes);
                    fileRepository.save(file);
                }
            };
            myRunnable.run();
        }
    }

    private byte[] getBytesFromWorkbook(Workbook workbook, FileStatus fileStatus) {
        try {
            return WorkbookUtils.getBytes(workbook);
        } catch (IOException e) {
            fileStatus.setStatus(Status.ERROR.name());
            fileStatusRepository.save(fileStatus);
            throw new RuntimeException("Can't read bytes from workbook!", e);
        }
    }

    private Workbook buildWorkbook(PriceCheckParameter parameter, FileStatus fileStatus) {
        try {
            Workbook workbook = priceCheckService.getWorkbook(parameter.getBytes(), parameter.getUrlColumn(),
                    parameter.getInsertColumn());
            fileStatus.setStatus(Status.COMPLETED.name());
            return workbook;
        } catch (IOException | InvalidFormatException e) {
            fileStatus.setStatus(Status.ERROR.name());
            throw new RuntimeException("Something goes wrong with checking price!", e);
        } finally {
            fileStatusRepository.save(fileStatus);
        }
    }

    private void createNewRecord(PriceCheckParameter parameter) {
        FileStatus fileStatus = new FileStatus();
        fileStatus.setName(parameter.getName());
        fileStatus.setStatus(Status.ACCEPTED.name());
        fileStatusRepository.save(fileStatus);
        File file = new File();
        file.setFile(parameter.getBytes());
        file.setFileStatus(fileStatus);
        fileRepository.save(file);
    }
}
