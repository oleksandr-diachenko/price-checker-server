package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.service.QueueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceRestController.class)
public class PriceRestControllerTest {

    public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
    public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String PRICECHECK = "/api/pricecheck";
    public static final String FILE = "file";
    public static final String URL_INDEX_PARAM = "urlIndex";
    public static final String INSERT_INDEX_PARAM = "insertIndex";
    private static final String FILENAME = "filename";
    private static final byte[] BYTES = new byte[]{1, 2, 3};
    private static final int URL_COLUMN = 1;
    private static final int URL_INDEX = 0;
    private static final int INSERT_COLUMN = 2;
    private static final int INSERT_INDEX = 1;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QueueService queueService;

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
}
