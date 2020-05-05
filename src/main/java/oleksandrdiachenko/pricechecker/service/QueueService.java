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
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;
import static oleksandrdiachenko.pricechecker.model.entity.Status.*;

/**
 * @author Alexander Diachenko
 */
@Service
@Slf4j
public class QueueService {

    private final FileRepository fileRepository;
    private final FileStatusRepository fileStatusRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PriceCheckService priceCheckService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final Queue<Pair<Long, PriceCheckParameter>> queue = new ConcurrentLinkedQueue<>();

    @Autowired
    public QueueService(FileRepository fileRepository, FileStatusRepository fileStatusRepository,
                        SimpMessagingTemplate simpMessagingTemplate, PriceCheckService priceCheckService) {
        this.fileRepository = fileRepository;
        this.fileStatusRepository = fileStatusRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.priceCheckService = priceCheckService;
    }

    public void addToQueue(PriceCheckParameter parameter) {
        long fileStatusId = createNewRecord(parameter);
        sendFileStatusesToWebSocket();
        queue.add(Pair.of(fileStatusId, parameter));
        executorService.submit(this::pickupNextFromQueue);
    }

    private void pickupNextFromQueue() {
        log.info("Queue size: {}.", queue.size());
        Pair<Long, PriceCheckParameter> poll = queue.poll();
        if (nonNull(poll)) {
            log.info("Picking next job from queue with file name: {}", poll.getRight().getName());
            fileStatusRepository.findById(poll.getLeft())
                    .ifPresent(fileStatus -> processWithPriceChecking(poll.getRight(), fileStatus));
        }
    }

    private void processWithPriceChecking(PriceCheckParameter parameter, FileStatus fileStatus) {
        updateStatus(fileStatus, PENDING);
        sendFileStatusesToWebSocket();

        fileRepository.findById(fileStatus.getFileId())
                .ifPresentOrElse(file -> {
                    byte[] bytes = WorkbookUtils.getBytes(buildWorkbook(parameter));
                    file.setFile(bytes);
                    fileRepository.save(file);
                    updateStatus(fileStatus, COMPLETED);
                    sendFileStatusesToWebSocket();
                }, () -> updateStatus(fileStatus, ERROR));
    }

    private void sendFileStatusesToWebSocket() {
        Iterable<FileStatus> fileStatuses = fileStatusRepository.findAll();
        log.info("Updating web socket statuses: {}", fileStatuses.toString());
        simpMessagingTemplate.convertAndSend("/statuses", fileStatuses);
    }

    private void updateStatus(FileStatus fileStatus, Status status) {
        log.info("For file status with id: {} updating status to {}", fileStatus.getId(), status.name());
        fileStatus.setStatus(status.name());
        fileStatusRepository.save(fileStatus);
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
        fileRepository.save(file);
        FileStatus fileStatus = new FileStatus();
        fileStatus.setName(parameter.getName());
        fileStatus.setStatus(ACCEPTED.name());
        fileStatus.setFileId(file.getId());
        fileStatus.setAcceptedTime(LocalDateTime.now());
        fileStatusRepository.save(fileStatus);
        return fileStatus.getId();
    }
}
