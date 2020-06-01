package oleksandrdiachenko.pricechecker.controller;

import com.google.common.collect.ImmutableMap;
import oleksandrdiachenko.pricechecker.model.PriceCheckParameter;
import oleksandrdiachenko.pricechecker.service.QueueService;
import oleksandrdiachenko.pricechecker.util.FileReader;
import oleksandrdiachenko.pricechecker.util.template.JsonTemplateResolver;
import oleksandrdiachenko.pricechecker.util.template.TemplateResolver;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore //TODO find how test secure methods
@ExtendWith(SpringExtension.class)
@WebMvcTest(PriceRestController.class)
public class PriceRestControllerTest {
//
//    private static final String XLS_CONTENT_TYPE = "application/vnd.ms-excel";
//    private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
//    private static final String PRICECHECK = "/api/pricecheck";
//    private static final String PARAMETER_MISSING_JSON = "file/template/json/parameterMissing.json";
//    private static final String COLUMN_NEGATIVE_JSON = "file/template/json/columnNegative.json";
//    private static final String BOTH_COLUMNS_NEGATIVE_JSON = "file/template/json/bothColumnsNegative.json";
//    private static final String FILE = "file";
//    private static final String URL_INDEX_PARAM = "urlIndex";
//    private static final String INSERT_INDEX_PARAM = "insertIndex";
//    private static final String FILENAME = "filename";
//    private static final byte[] BYTES = new byte[]{1, 2, 3};
//    private static final int URL_COLUMN = 1;
//    private static final int URL_INDEX = 0;
//    private static final int INSERT_COLUMN = 2;
//    private static final int INSERT_INDEX = 1;
//    private final TemplateResolver resolver = new JsonTemplateResolver();
//    private final FileReader fileReader = new FileReader();
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private QueueService queueService;
//
//    @Test
//    void shouldReturnOkAndCallServiceWhenFileIsXls() throws Exception {
//        mvc.perform(multipart(PRICECHECK)
//                .file(new MockMultipartFile(FILE, FILENAME,
//                        XLS_CONTENT_TYPE, BYTES))
//                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN))
//                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
//                .andExpect(status().isAccepted())
//                .andExpect(content().string(EMPTY));
//        verify(queueService).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
//    }
//
//    @Test
//    void shouldReturnOkAndCallServiceWhenFileIsXlsx() throws Exception {
//        mvc.perform(multipart(PRICECHECK)
//                .file(new MockMultipartFile(FILE, FILENAME,
//                        XLSX_CONTENT_TYPE, BYTES))
//                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN))
//                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
//                .andExpect(status().isAccepted())
//                .andExpect(content().string(EMPTY));
//        verify(queueService).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenUrlIndexParameterNotSet() throws Exception {
//        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
//                .put("missingParameter", "urlIndex")
//                .build();
//        String jsonContent = resolver.resolve(fileReader.read(PARAMETER_MISSING_JSON), parameters);
//        mvc.perform(multipart(PRICECHECK)
//                .file(new MockMultipartFile(FILE, FILENAME,
//                        XLSX_CONTENT_TYPE, BYTES))
//                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(jsonContent));
//        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenInsertIndexParameterNotSet() throws Exception {
//        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
//                .put("missingParameter", "insertIndex")
//                .build();
//        String jsonContent = resolver.resolve(fileReader.read(PARAMETER_MISSING_JSON), parameters);
//        mvc.perform(multipart(PRICECHECK)
//                .file(new MockMultipartFile(FILE, FILENAME,
//                        XLSX_CONTENT_TYPE, BYTES))
//                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(jsonContent));
//        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenUrlIndexIsZero() throws Exception {
//        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
//                .put("columnName", "Url")
//                .build();
//        String jsonContent = resolver.resolve(fileReader.read(COLUMN_NEGATIVE_JSON), parameters);
//        mvc.perform(multipart(PRICECHECK)
//                .file(new MockMultipartFile(FILE, FILENAME,
//                        XLSX_CONTENT_TYPE, BYTES))
//                .param(URL_INDEX_PARAM, "0")
//                .param(INSERT_INDEX_PARAM, String.valueOf(INSERT_COLUMN)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(jsonContent));
//        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenInsertIndexIsZero() throws Exception {
//        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
//                .put("columnName", "Insert")
//                .build();
//        String jsonContent = resolver.resolve(fileReader.read(COLUMN_NEGATIVE_JSON), parameters);
//        mvc.perform(multipart(PRICECHECK)
//                .file(new MockMultipartFile(FILE, FILENAME,
//                        XLSX_CONTENT_TYPE, BYTES))
//                .param(URL_INDEX_PARAM, String.valueOf(URL_COLUMN))
//                .param(INSERT_INDEX_PARAM, "0"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(jsonContent));
//        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenInsertAndUrlIndexesIsZero() throws Exception {
//        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
//                .put("firstColumnName", "Url")
//                .put("secondColumnName", "Insert")
//                .build();
//        String jsonContent = resolver.resolve(fileReader.read(BOTH_COLUMNS_NEGATIVE_JSON), parameters);
//        mvc.perform(multipart(PRICECHECK)
//                .file(new MockMultipartFile(FILE, FILENAME,
//                        XLSX_CONTENT_TYPE, BYTES))
//                .param(URL_INDEX_PARAM, "0")
//                .param(INSERT_INDEX_PARAM, "0"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().json(jsonContent));
//        verify(queueService, never()).addToQueue(new PriceCheckParameter(FILENAME, URL_INDEX, INSERT_INDEX, BYTES));
//    }
}
