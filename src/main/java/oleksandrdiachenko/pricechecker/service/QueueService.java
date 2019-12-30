package oleksandrdiachenko.pricechecker.service;

import com.google.common.annotations.VisibleForTesting;
import com.google.gson.Gson;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import oleksandrdiachenko.pricechecker.util.WorkbookUtils;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
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

    private Queue<Pair<Long, PriceCheckParameter>> queue = new ConcurrentLinkedQueue<>();

    @Autowired
    public QueueService(PriceCheckService priceCheckService, FileRepository fileRepository,
                        FileStatusRepository fileStatusRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.priceCheckService = priceCheckService;
        this.fileRepository = fileRepository;
        this.fileStatusRepository = fileStatusRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void start(PriceCheckParameter parameter) {
        long fileStatusId = createNewRecord(parameter);
        queue.add(Pair.create(fileStatusId, parameter));
        while (!queue.isEmpty()) {
            Runnable myRunnable = () -> {
                Pair<Long, PriceCheckParameter> poll = queue.poll();
                if (poll == null) {
                    return;
                }
                Optional<FileStatus> fileStatusOptional = fileStatusRepository.findById(poll.getFirst());
                if (fileStatusOptional.isPresent()) {
                    FileStatus fileStatus = fileStatusOptional.get();
                    updateStatus(fileStatus, Status.PENDING.name());

                    Workbook workbook = buildWorkbook(poll.getSecond());

                    updateStatus(fileStatus, Status.COMPLETED.name());
                    Optional<File> fileOptional = fileRepository.findById(fileStatus.getFileId());
                    if (fileOptional.isPresent()) {
                        File file = fileOptional.get();
                        byte[] bytes = getBytesFromWorkbook(workbook);
                        file.setFile(bytes);
                        fileRepository.save(file);
                        simpMessagingTemplate.convertAndSend("/statuses",
                                new Gson().toJson(fileStatusRepository.findAll()));
                    }
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
    protected long createNewRecord(PriceCheckParameter parameter) {
        File file = new File();
        file.setFile(parameter.getBytes());
        fileRepository.save(file);
        FileStatus fileStatus = new FileStatus();
        fileStatus.setName(parameter.getName());
        fileStatus.setStatus(Status.ACCEPTED.name());
        fileStatus.setFileId(file.getId());
        fileStatusRepository.save(fileStatus);
        return fileStatus.getId();
    }
}
