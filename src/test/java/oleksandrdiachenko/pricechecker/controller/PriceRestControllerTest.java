package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.service.MainService;
import oleksandrdiachenko.pricechecker.service.PriceCheckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceRestController.class)
public class PriceRestControllerTest {

    public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
    public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String PRICECHECK = "/pricecheck";
    public static final String FILE = "file";
    public static final String URL_INDEX_PARAM = "urlIndex";
    public static final String INSERT_INDEX_PARAM = "insertIndex";
    private static String FILENAME = "filename";
    private static byte[] BYTES = new byte[]{1, 2, 3};
    private static int URL_INDEX = 1;
    private static int INSERT_INDEX = 2;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MainService mainService;
    @MockBean
    private PriceCheckService priceCheckService;

    @Test
    void shouldAcceptMultipartPostRequestWhenFileIsXls() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLS_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_INDEX))
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_INDEX)))
                .andExpect(status().isAccepted());
        verify(mainService).start(BYTES, URL_INDEX, INSERT_INDEX);
    }

    @Test
    void shouldAcceptMultipartPostRequestWhenFileIsXlsx() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_INDEX))
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_INDEX)))
                .andExpect(status().isAccepted());
        verify(mainService).start(BYTES, URL_INDEX, INSERT_INDEX);
    }

    @Test
    void shouldRejectMultipartPostRequestWhenFileTypeIsJpg() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        IMAGE_JPEG_VALUE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_INDEX))
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_INDEX)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("File extension should be .xls or .xlsx"));
        verify(mainService, never()).start(BYTES, URL_INDEX, INSERT_INDEX);
    }

    @Test
    void shouldRejectMultipartPostRequestWhenUrlIndexParameterNotSet() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_INDEX)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required int parameter 'urlIndex' is not present"));
        verify(mainService, never()).start(BYTES, URL_INDEX, INSERT_INDEX);
    }

    @Test
    void shouldRejectMultipartPostRequestWhenInsertIndexParameterNotSet() throws Exception {
        mvc.perform(multipart(PRICECHECK)
                .file(new MockMultipartFile(FILE, FILENAME,
                        XLSX_CONTENT_TYPE, BYTES))
                .param(URL_INDEX_PARAM, String.valueOf(URL_INDEX)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required int parameter 'insertIndex' is not present"));
        verify(mainService, never()).start(BYTES, URL_INDEX, INSERT_INDEX);
    }
}
