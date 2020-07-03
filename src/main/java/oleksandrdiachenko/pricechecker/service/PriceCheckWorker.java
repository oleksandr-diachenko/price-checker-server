package oleksandrdiachenko.pricechecker.service;

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
import oleksandrdiachenko.pricechecker.util.WorkbookHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;

import static oleksandrdiachenko.pricechecker.model.entity.Status.*;

@Log4j2
@RequiredArgsConstructor
@Service
public class PriceCheckWorker {

    private final FileService fileService;
    private final FileStatusService fileStatusService;
    private final PriceCheckService priceCheckService;
    private final WorkbookHelper workbookHelper;
    private final Set<Notification<Status>> statusNotifications;

    @Transactional
    public void run(long fileStatusId, PriceCheckParameter parameter) {
        log.info("Start work for file with name: {}", parameter.getName());
        fileStatusService.findById(fileStatusId)
                .ifPresent(fileStatus -> processWithPriceChecking(parameter, fileStatus));
    }

    private void processWithPriceChecking(PriceCheckParameter parameter, FileStatus fileStatus) {
        updateStatus(fileStatus, IN_PROGRESS);

        fileService.findById(fileStatus.getFileId())
                .ifPresentOrElse(file -> {
                    byte[] bytes = workbookHelper.getBytes(buildWorkbook(parameter));
                    file.setFile(bytes);
                    fileService.save(file);
                    updateStatus(fileStatus, COMPLETED);
                }, () -> updateStatus(fileStatus, ERROR));
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

    private Workbook buildWorkbook(PriceCheckParameter parameter) {
        try {
            return priceCheckService.getWorkbook(parameter.getBytes(), parameter.getUrlColumn(),
                    parameter.getInsertColumn());
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException("Something goes wrong with checking price!", e);
        }
    }
}
