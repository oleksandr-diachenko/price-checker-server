package oleksandrdiachenko.pricechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.dataservice.FileService;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import oleksandrdiachenko.pricechecker.service.dataservice.UserService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static oleksandrdiachenko.pricechecker.model.entity.Status.ACCEPTED;

/**
 * @author Alexander Diachenko
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class QueueService {

    private final FileService fileService;
    private final FileStatusService fileStatusService;
    private final PriceCheckWorker priceCheckWorker;
    private final ExecutorService executorService;
    private final UserService userService;

    private final Queue<Pair<Long, PriceCheckParameter>> queue = new ConcurrentLinkedQueue<>();

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
        Optional<User> userOptional = userService.findById(parameter.getUserId());
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException(format("User with id %S not found", parameter.getUserId()));
        }
        File file = new File();
        file.setUser(userOptional.get());
        fileService.save(file);
        FileStatus fileStatus = new FileStatus();
        fileStatus.setName(parameter.getName());
        fileStatus.setStatus(ACCEPTED.name());
        fileStatus.setFileId(file.getId());
        fileStatus.setAcceptedTime(LocalDateTime.now());
        fileStatus.setUser(userOptional.get());
        fileStatusService.save(fileStatus);
        return fileStatus.getId();
    }
}
