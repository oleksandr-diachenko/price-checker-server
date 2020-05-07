package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.util.WorkbookHelper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessagingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static oleksandrdiachenko.pricechecker.model.entity.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PriceCheckWorkerTest {

    private static final String FILE_NAME = "name.xlsx";
    private static final byte[] BYTES = {1, 2, 3};
    private static final long FILE_STATUS_ID = 1;
    private static final long FILE_ID = 2;
    private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2020, 5, 5, 5, 26, 12);
    private static final PriceCheckParameter PARAMETER = new PriceCheckParameter(FILE_NAME, 1, 2, BYTES);

    @InjectMocks
    private PriceCheckWorker priceCheckWorker;

    @Mock
    private FileService fileService;
    @Mock
    private FileStatusService fileStatusService;
    @Mock
    private PriceCheckService priceCheckService;
    @Mock
    private WebSocketService webSocketService;
    @Mock
    private WorkbookHelper workbookHelper;
    @Mock
    private Workbook workbook;
    @Captor
    private ArgumentCaptor<FileStatus> captor;

    @BeforeEach
    void setUp() {
        when(fileStatusService.findById(FILE_STATUS_ID)).thenReturn(Optional.of(createFileStatus(ACCEPTED)));
    }

    @Test
    void shouldNotUpdateRecordWhenFileStatusNotExist() {
        when(fileStatusService.findById(FILE_STATUS_ID)).thenReturn(Optional.empty());

        priceCheckWorker.run(FILE_STATUS_ID, PARAMETER);

        verify(fileStatusService, never()).save(any(FileStatus.class));
        verify(webSocketService, never()).sendFileStatusesToWebSocket();
    }

    @Test
    void shouldUpdateToComplete() throws IOException, InvalidFormatException {
        when(fileService.findById(FILE_ID)).thenReturn(Optional.of(createFile()));
        when(priceCheckService.getWorkbook(PARAMETER.getBytes(), PARAMETER.getUrlColumn(), PARAMETER.getInsertColumn())).thenReturn(workbook);
        when(workbookHelper.getBytes(workbook)).thenReturn(BYTES);

        priceCheckWorker.run(FILE_STATUS_ID, PARAMETER);

        verify(fileStatusService, times(2)).save(captor.capture());
        verify(webSocketService, times(2)).sendFileStatusesToWebSocket();
        assertThat(captor.getValue()).isEqualTo(createFileStatus(COMPLETED));
    }

    @Test
    void shouldUpdateToErrorWhenFileNotExist() {
        when(fileService.findById(FILE_ID)).thenReturn(Optional.empty());

        priceCheckWorker.run(FILE_STATUS_ID, PARAMETER);

        verify(fileStatusService, times(2)).save(captor.capture());
        verify(webSocketService).sendFileStatusesToWebSocket();
        assertThat(captor.getValue()).isEqualTo(createFileStatus(ERROR));
    }

    @Test
    void shouldUpdateToPendingWhenWebSocketThrowException() {
        doThrow(MessagingException.class).when(webSocketService).sendFileStatusesToWebSocket();

        try {
            priceCheckWorker.run(FILE_STATUS_ID, PARAMETER);
        } catch (MessagingException e) {
            verify(fileStatusService).save(captor.capture());
            assertThat(captor.getValue()).isEqualTo(createFileStatus(IN_PROGRESS));
        }
    }

    private FileStatus createFileStatus(Status status) {
        FileStatus fileStatus = new FileStatus();
        fileStatus.setId(FILE_STATUS_ID);
        fileStatus.setName(FILE_NAME);
        fileStatus.setStatus(status.name());
        fileStatus.setAcceptedTime(LOCAL_DATE_TIME);
        fileStatus.setFileId(FILE_ID);
        return fileStatus;
    }

    private File createFile() {
        File file = new File();
        file.setId(FILE_ID);
        file.setFile(BYTES);
        return file;
    }
}