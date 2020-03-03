package oleksandrdiachenko.pricechecker.service;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Alexander Diachenko
 */
@Service
@Slf4j
public class QueueService {

    private FileRepository fileRepository;
    private FileStatusRepository fileStatusRepository;
    private SimpMessagingTemplate simpMessagingTemplate;
    private PriceCheckService priceCheckService;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private Queue<Pair<Long, PriceCheckParameter>> queue = new ConcurrentLinkedQueue<>();

    @Autowired
    public QueueService(FileRepository fileRepository,
                        FileStatusRepository fileStatusRepository, SimpMessagingTemplate simpMessagingTemplate, PriceCheckService priceCheckService) {
        this.fileRepository = fileRepository;
        this.fileStatusRepository = fileStatusRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.priceCheckService = priceCheckService;
    }

    public void start(PriceCheckParameter parameter) {
        long fileStatusId = createNewRecord(parameter);
        sendFileStatusesToWebSocket();
        queue.add(Pair.create(fileStatusId, parameter));
        executorService.submit(() -> {
            log.info("Queue size: {}.", queue.size());
            Pair<Long, PriceCheckParameter> poll = queue.poll();
            if (poll == null) {
                return;
            }
            log.info("Picking next job from queue with file name: {}", poll.getSecond().getName());
            Optional<FileStatus> fileStatusOptional = fileStatusRepository.findById(poll.getFirst());
            if (fileStatusOptional.isPresent()) {
                FileStatus fileStatus = fileStatusOptional.get();
                updateStatus(fileStatus, Status.PENDING.name());
                sendFileStatusesToWebSocket();

                Workbook workbook = buildWorkbook(poll.getSecond());

                updateStatus(fileStatus, Status.COMPLETED.name());
                Optional<File> fileOptional = fileRepository.findById(fileStatus.getFileId());
                if (fileOptional.isPresent()) {
                    File file = fileOptional.get();
                    byte[] bytes = getBytesFromWorkbook(workbook);
                    file.setFile(bytes);
                    fileRepository.save(file);
                    sendFileStatusesToWebSocket();
                }
            }
        });
    }

    private void sendFileStatusesToWebSocket() {
        Iterable<FileStatus> fileStatuses = fileStatusRepository.findAll();
        log.info("Updating web socket statuses: {}", fileStatuses.toString());
        simpMessagingTemplate.convertAndSend("/statuses", fileStatuses);
    }

    private void updateStatus(FileStatus fileStatus, String status) {
        log.info("For file status with id: [{}] updating status to [{}]", fileStatus.getId(), status);
        fileStatus.setStatus(status);
        fileStatusRepository.save(fileStatus);
    }

    private byte[] getBytesFromWorkbook(Workbook workbook) {
        try {
            return WorkbookUtils.getBytes(workbook);
        } catch (IOException e) {
            log.error("Can't read bytes from workbook!");
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
        fileStatus.setAcceptedTime(LocalDateTime.now());
        fileStatusRepository.save(fileStatus);
        return fileStatus.getId();
    }
}
