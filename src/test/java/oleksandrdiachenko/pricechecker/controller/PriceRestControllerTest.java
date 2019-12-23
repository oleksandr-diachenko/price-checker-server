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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static java.lang.String.format;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceRestController.class)
@EnableWebMvc
public class PriceRestControllerTest {

    public static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
    public static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MainService mainService;
    @MockBean
    private PriceCheckService priceCheckService;

    @Test
    void shouldAcceptMultipartPostRequestWhenFileIsXls() throws Exception {
        String fileName = "filename.xls";
        byte[] bytes = "some xls".getBytes();
        int urlIndex = 1;
        int insertIndex = 2;

        mvc.perform(multipart("/pricecheck")
                .file(new MockMultipartFile("file", fileName,
                        XLS_CONTENT_TYPE, bytes))
                .param("urlIndex", String.valueOf(urlIndex))
                .param("insertIndex", String.valueOf(insertIndex)))
                .andExpect(content().string(
                        format("File: %s with url index : %d and insert index: %d accepted.",
                                fileName, urlIndex, insertIndex)
                ))
                .andExpect(status().isAccepted());
        verify(mainService).start(bytes, urlIndex, insertIndex);
    }

    @Test
    void shouldAcceptMultipartPostRequestWhenFileIsXlsx() throws Exception {
        String fileName = "filename.xlsx";
        byte[] bytes = "some xlsx".getBytes();
        int urlIndex = 1;
        int insertIndex = 2;

        mvc.perform(multipart("/pricecheck")
                .file(new MockMultipartFile("file", fileName,
                        XLSX_CONTENT_TYPE, bytes))
                .param("urlIndex", String.valueOf(urlIndex))
                .param("insertIndex", String.valueOf(insertIndex)))
                .andExpect(content().string(
                        format("File: %s with url index : %d and insert index: %d accepted.",
                                fileName, urlIndex, insertIndex)
                ))
                .andExpect(status().isAccepted());
        verify(mainService).start(bytes, urlIndex, insertIndex);
    }

    @Test
    void shouldRejectMultipartPostRequestWhenFileTypeIsJpg() throws Exception {
        String fileName = "filename.jpg";
        byte[] bytes = "some jpg".getBytes();
        int urlIndex = 1;
        int insertIndex = 2;

        mvc.perform(multipart("/pricecheck")
                .file(new MockMultipartFile("file", fileName,
                        IMAGE_JPEG_VALUE, bytes))
                .param("urlIndex", String.valueOf(urlIndex))
                .param("insertIndex", String.valueOf(insertIndex)))
                .andExpect(content().string("File extension should be .xls or .xlsx"))
                .andExpect(status().isBadRequest());
        verify(mainService, times(0)).start(bytes, urlIndex, insertIndex);
    }
}
