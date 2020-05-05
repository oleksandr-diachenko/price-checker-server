package oleksandrdiachenko.pricechecker.service;

import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class WebSocketService {

    private static final String DESTINATION = "/statuses";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FileStatusService fileStatusService;


    @Autowired
    public WebSocketService(SimpMessagingTemplate simpMessagingTemplate, FileStatusService fileStatusService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.fileStatusService = fileStatusService;
    }

    public void sendFileStatusesToWebSocket() {
        Iterable<FileStatus> fileStatuses = fileStatusService.findAll();
        log.info("Updating web socket statuses: {}", fileStatuses.toString());
        simpMessagingTemplate.convertAndSend(DESTINATION, fileStatuses);
    }
}
