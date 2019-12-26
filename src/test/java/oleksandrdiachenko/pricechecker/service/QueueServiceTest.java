package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import oleksandrdiachenko.pricechecker.util.WorkbookUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author Alexander Diachenko
 */
@ExtendWith(MockitoExtension.class)
public class QueueServiceTest {

    private static final byte[] BYTES = {1, 2, 3};
    private static final int URL_COLUMN = 1;
    private static final int INSERT_COLUMN = 2;
    private static final String FILE_NAME = "fileName";

    @InjectMocks
    @Spy
    private QueueService queueService;

    @Mock
    private PriceCheckService priceCheckService;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileStatusRepository fileStatusRepository;

    @Test
    void  efsfef() throws IOException, InvalidFormatException {
        Workbook workbook = new SXSSFWorkbook();
        when(priceCheckService.getWorkbook(BYTES, URL_COLUMN,INSERT_COLUMN)).thenReturn(workbook);
        FileStatus fileStatus = createFileStatus();
        File file = createFile(fileStatus);
        when(fileStatusRepository.findByName(FILE_NAME)).thenReturn(fileStatus);
        when(fileRepository.findByFileStatusId(1)).thenReturn(file);
        PriceCheckParameter parameter = new PriceCheckParameter(FILE_NAME, URL_COLUMN, INSERT_COLUMN, BYTES);

        queueService.start(parameter);

        verify(queueService).createNewRecord(parameter);
//        verify(fileStatusRepository).save(fileStatus);
        verify(priceCheckService).getWorkbook(BYTES, URL_COLUMN, INSERT_COLUMN);
        fileStatus.setStatus(Status.COMPLETED.name());
//        verify(fileStatusRepository).save(fileStatus);
        file.setFile(WorkbookUtils.getBytes(workbook));
        verify(fileRepository).save(file);
    }

    private File createFile(FileStatus fileStatus) {
        File file = new File();
        file.setId(1);
        file.setFile(BYTES);
        file.setFileStatus(fileStatus);
        return file;
    }

    private FileStatus createFileStatus() {
        FileStatus fileStatus = new FileStatus();
        fileStatus.setId(1);
        fileStatus.setName(FILE_NAME);
        fileStatus.setStatus(Status.ACCEPTED.name());
        return fileStatus;
    }
}
