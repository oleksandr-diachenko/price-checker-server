package oleksandrdiachenko.pricechecker.service;

import com.epam.pricecheckercore.exception.PriceCheckerException;
import com.epam.pricecheckercore.model.inputoutput.CheckerInput;
import com.epam.pricecheckercore.service.checker.Checker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.dataservice.FileService;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import oleksandrdiachenko.pricechecker.service.notification.Notification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

import static oleksandrdiachenko.pricechecker.model.entity.Status.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class PriceCheckWorker {

    private final FileService fileService;
    private final FileStatusService fileStatusService;
    private final Set<Notification<Status>> statusNotifications;
    private final Checker checker;

    @Async
    public void run(long fileStatusId, PriceCheckParameter parameter) {
        log.info("Start work for file with name: {}", parameter.getName());
        fileStatusService.findById(fileStatusId)
                .ifPresent(fileStatus -> processWithPriceChecking(parameter, fileStatus));
    }

    private void processWithPriceChecking(PriceCheckParameter parameter, FileStatus fileStatus) {
        updateStatus(fileStatus, IN_PROGRESS);
        CheckerInput checkerInput = CheckerInput.builder()
                .file(parameter.getBytes())
                .urlIndex(parameter.getUrlColumn() - 1)
                .insertIndex(parameter.getInsertColumn() - 1).build();

        fileService.findById(fileStatus.getFileId())
                .ifPresentOrElse(file -> {
                    byte[] bytes = getBytes(checkerInput);
                    file.setFile(bytes);
                    fileService.save(file);
                    updateStatus(fileStatus, COMPLETED);
                }, () -> updateStatus(fileStatus, ERROR));
    }

    private byte[] getBytes(CheckerInput checkerInput) {
        try {
            return checker.check(checkerInput).getFile();
        } catch (PriceCheckerException e) {
            throw new RuntimeException("Cant check");
        }
    }

    private void updateStatus(FileStatus fileStatus, Status status) {
        log.info("For file status with id: {} updating status to {}", fileStatus.getId(), status.name());
        fileStatus.setStatus(status.name());
        fileStatusService.save(fileStatus);
        fileService.findById(fileStatus.getFileId())
                .ifPresentOrElse(file -> notifyUser(fileStatus.getUser(), status, file),
                        () -> notifyUser(fileStatus.getUser(), status));

    }

    private void notifyUser(User user, Status status, File... files) {
        statusNotifications.forEach(notification -> notification.notify(user, status, files));
    }
}
