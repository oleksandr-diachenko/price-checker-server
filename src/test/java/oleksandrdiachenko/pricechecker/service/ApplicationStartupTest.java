package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.repository.FileRepository;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicationStartupTest {

    @InjectMocks
    private ApplicationStartup applicationStartup;

    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileStatusRepository fileStatusRepository;
    @Mock
    private ApplicationReadyEvent applicationReadyEvent;

    @Test
    void shouldClearFileAndFileStatusTablesWhenApplicationReady() {
        applicationStartup.onApplicationEvent(applicationReadyEvent);

        verify(fileRepository).deleteAll();
        verify(fileStatusRepository).deleteAll();
    }
}