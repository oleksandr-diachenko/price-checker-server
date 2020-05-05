package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.service.FileService;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.QueueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceRestController.class)
public class PriceRestControllerTest {

    public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
    public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String PRICECHECK = "/api/pricecheck";
    public static final String FILESTATUSES = "/api/pricecheck/filestatuses";
    public static final String FILE_BY_ID = "/api/pricecheck/file/";
    public static final String FILE = "file";
    public static final String URL_INDEX_PARAM = "urlIndex";
    public static final String INSERT_INDEX_PARAM = "insertIndex";
    private static String FILENAME = "filename";
    private static byte[] BYTES = new byte[]{1, 2, 3};
    private static int URL_COLUMN = 1;
    private static int URL_INDEX = 0;
    private static int INSERT_COLUMN = 2;
    private static int INSERT_INDEX = 1;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QueueService queueService;
    @MockBean
    private FileStatusService fileStatusService;
    @MockBean
    private FileService fileService;
    @Mock
    private File file;

    @Test
    void shouldReturnOkAndCallServiceWhenFileIsXls() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLS_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN))
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(EMPTY));
        verify(queueService).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
    }

    @Test
    void shouldReturnOkAndCallServiceWhenFileIsXlsx() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN))
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
                .andExpect(status().isAccepted())
                .andExpect(content().string(EMPTY));
        verify(queueService).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
    }

    @Test
    void shouldReturnBadRequestWhenUrlIndexParameterNotSet() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"urlIndex parameter is missing.\"]}"));
        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
    }

    @Test
    void shouldReturnBadRequestWhenInsertIndexParameterNotSet() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"insertIndex parameter is missing.\"]}"));
        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
    }

    @Test
    void shouldReturnBadRequestWhenUrlIndexIsZero() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, "0")
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Url column should be greater than 0\"]}"));
        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
    }

    @Test
    void shouldReturnBadRequestWhenInsertIndexIsZero() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN))
                .param(INSERT_INDEX_PARAM, "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Insert column should be greater than 0\"]}"));
        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
    }

    @Test
    void shouldReturnBadRequestWhenInsertAndUrlIndexesIsZero() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, "0")
                .param(INSERT_INDEX_PARAM, "0"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Url column should be greater than 0\"," +
                        "\"Insert column should be greater than 0\"]}"));
        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
    }

    @Test
    void shouldReturnListWithOneFileStatusesWhenServiceReturnListWithOneFileStatus() throws Exception {
        when(fileStatusService.findAll()).thenReturn(Collections.singletonList(
                new FileStatus(1, FILENAME, Status.COMPLETED.name(), 2,
                        LocalDateTime.of(2020, 1, 6, 5, 18, 20))));

        mvc.perform(get(FILESTATUSES))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"filename\"," +
                        "\"status\":\"COMPLETED\",\"fileId\":2,\"acceptedTime\":\"2020-01-06T05:18:20\"}]"));
    }

    @Test
    void shouldReturnEmptyListWhenServiceReturnEmptyList() throws Exception {
        when(fileStatusService.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get(FILESTATUSES))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnBytesWhenFileExist() throws Exception {
        long fileId = 1;
        when(fileService.findById(fileId)).thenReturn(Optional.of(file));
        when(file.getFile()).thenReturn(BYTES);

        mvc.perform(get(FILE_BY_ID + fileId))
                .andExpect(status().isOk())
                .andExpect(content().bytes(file.getFile()));
    }

    @Test
    void shouldReturnNotFoundWhenFileIsNotExist() throws Exception {
        long fileId = -1;
        when(fileService.findById(fileId)).thenReturn(Optional.empty());

        mvc.perform(get(FILE_BY_ID + fileId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"errors\":[\"File with id: -1 not found\"]}"));
    }
}
