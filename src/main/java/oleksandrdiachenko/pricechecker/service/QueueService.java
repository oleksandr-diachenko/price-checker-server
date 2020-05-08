package oleksandrdiachenko.pricechecker.service;

import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import static java.util.Objects.nonNull;
import static oleksandrdiachenko.pricechecker.model.entity.Status.ACCEPTED;

/**
 * @author Alexander Diachenko
 */
@Service
@Slf4j
public class QueueService {

    private final FileService fileService;
    private final FileStatusService fileStatusService;
    private final PriceCheckWorker priceCheckWorker;
    private final ExecutorService executorService;

    private final Queue<Pair<Long, PriceCheckParameter>> queue = new ConcurrentLinkedQueue<>();

    @Autowired
    public QueueService(FileService fileService, FileStatusService fileStatusService,
                        PriceCheckWorker priceCheckWorker, ExecutorService executorService) {
        this.fileService = fileService;
        this.fileStatusService = fileStatusService;
        this.priceCheckWorker = priceCheckWorker;
        this.executorService = executorService;
    }

    public void addToQueue(PriceCheckParameter parameter) {
        long fileStatusId = createNewRecord(parameter);
        queue.add(Pair.of(fileStatusId, parameter));
        executorService.submit(() -> {
            log.info("Queue size: {}.", queue.size());
            Pair<Long, PriceCheckParameter> poll = queue.poll();
            if (nonNull(poll)) {
                priceCheckWorker.run(poll.getLeft(), poll.getRight());
            }
        });
    }

    private long createNewRecord(PriceCheckParameter parameter) {
        File file = new File();
        fileService.save(file);
        FileStatus fileStatus = new FileStatus();
        fileStatus.setName(parameter.getName());
        fileStatus.setStatus(ACCEPTED.name());
        fileStatus.setFileId(file.getId());
        fileStatus.setAcceptedTime(LocalDateTime.now());
        fileStatusService.save(fileStatus);
        return fileStatus.getId();
    }
}
