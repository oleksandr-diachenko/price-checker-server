package oleksandrdiachenko.pricechecker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebSocketServiceTest {

    private static final String DESTINATION = "/statuses";

    @InjectMocks
    private WebSocketService webSocketService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private FileStatusService fileStatusService;

    @Test
    void shouldSendFileStatusesToStatusesEndpoint() {
        when(fileStatusService.findAll()).thenReturn(Collections.emptyList());

        webSocketService.sendFileStatusesToWebSocket();

        verify(simpMessagingTemplate).convertAndSend(DESTINATION, Collections.emptyList());
    }
}
