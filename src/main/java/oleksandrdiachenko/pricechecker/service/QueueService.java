package oleksandrdiachenko.pricechecker.service;

import com.google.common.annotations.VisibleForTesting;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
    private SimpMessagingTemplate simpMessagingTemplate;

    private Queue<PriceCheckParameter> queue = new ConcurrentLinkedQueue<>();

    @Autowired
    public QueueService(PriceCheckService priceCheckService, FileRepository fileRepository,
                        FileStatusRepository fileStatusRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.priceCheckService = priceCheckService;
        this.fileRepository = fileRepository;
        this.fileStatusRepository = fileStatusRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void start(PriceCheckParameter parameter) {
        queue.add(parameter);
        createNewRecord(parameter);
        while (!queue.isEmpty()) {
            Runnable myRunnable = () -> {
                PriceCheckParameter poll = queue.poll();
                if (poll != null) {
                    FileStatus fileStatus = fileStatusRepository.findByName(poll.getName());
                    updateStatus(fileStatus, Status.PENDING.name());

                    Workbook workbook = buildWorkbook(poll);

                    updateStatus(fileStatus, Status.COMPLETED.name());
                    File file = fileRepository.findByFileStatusId(fileStatus.getId());
                    byte[] bytes = getBytesFromWorkbook(workbook);
                    file.setFile(bytes);
                    fileRepository.save(file);
                    simpMessagingTemplate.convertAndSend("/chat", "Completed: " + poll.getName());
                }
            };
            myRunnable.run();
        }
    }

    private void updateStatus(FileStatus fileStatus, String status) {
        fileStatus.setStatus(status);
        fileStatusRepository.save(fileStatus);
    }

    private byte[] getBytesFromWorkbook(Workbook workbook) {
        try {
            return WorkbookUtils.getBytes(workbook);
        } catch (IOException e) {
            throw new RuntimeException("Can't read bytes from workbook!", e);
        }
    }

    private Workbook buildWorkbook(PriceCheckParameter parameter) {
        try {
            return priceCheckService.getWorkbook(parameter.getBytes(), parameter.getUrlColumn(),
                    parameter.getInsertColumn());
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException("Something goes wrong with checking price!", e);
        }
    }

    @VisibleForTesting
    protected void createNewRecord(PriceCheckParameter parameter) {
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
