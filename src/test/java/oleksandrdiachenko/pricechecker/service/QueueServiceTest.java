package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.repository.UserData;
import oleksandrdiachenko.pricechecker.service.dataservice.FileService;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import oleksandrdiachenko.pricechecker.service.dataservice.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static oleksandrdiachenko.pricechecker.model.entity.Status.ACCEPTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/6/2020
 **/
@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    private static final String NAME = "filename.xls";
    private static final int URL_COLUMN = 1;
    private static final int INSERT_COLUMN = 2;
    private static final byte[] BYTES = new byte[]{1, 2, 3};
    private static final long USER_ID = 5L;

    @InjectMocks
    private QueueService queueService;

    @Mock
    private FileService fileService;
    @Mock
    private FileStatusService fileStatusService;
    @Mock
    private PriceCheckWorker priceCheckWorker;
    @Mock
    private UserService userService;
    @Captor
    private ArgumentCaptor<File> fileCaptor;
    @Captor
    private ArgumentCaptor<FileStatus> fileStatusCaptor;

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        PriceCheckParameter parameter = new PriceCheckParameter(NAME, URL_COLUMN, INSERT_COLUMN, BYTES, USER_ID);
        when(userService.findById(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> queueService.addToQueue(parameter)).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("User with id 5 not found");
    }

    @Test
    void shouldCreateNewFileAndFilStatusRecordAndRunWorker() {
        PriceCheckParameter parameter = new PriceCheckParameter(NAME, URL_COLUMN, INSERT_COLUMN, BYTES, USER_ID);
        when(userService.findById(USER_ID)).thenReturn(Optional.of(UserData.get()));

        queueService.addToQueue(parameter);

        verify(fileService).save(fileCaptor.capture());
        assertThat(fileCaptor.getValue().getUser()).isEqualTo(UserData.get());
        verify(fileStatusService).save(fileStatusCaptor.capture());
        assertThat(fileStatusCaptor.getValue().getName()).isEqualTo(NAME);
        assertThat(fileStatusCaptor.getValue().getStatus()).isEqualTo(ACCEPTED.name());
        assertThat(fileStatusCaptor.getValue().getFileId()).isEqualTo(fileCaptor.getValue().getId());
        assertThat(fileStatusCaptor.getValue().getUser()).isEqualTo(UserData.get());

        verify(priceCheckWorker).run(fileStatusCaptor.getValue().getId(), parameter);
    }
}