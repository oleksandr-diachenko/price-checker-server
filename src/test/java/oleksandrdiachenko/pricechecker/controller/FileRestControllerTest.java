package oleksandrdiachenko.pricechecker.controller;

import com.google.common.collect.ImmutableMap;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.service.FileService;
import oleksandrdiachenko.pricechecker.util.FileReader;
import oleksandrdiachenko.pricechecker.util.template.JsonTemplateResolver;
import oleksandrdiachenko.pricechecker.util.template.TemplateResolver;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO find how test secure methods
@ExtendWith(SpringExtension.class)
@WebMvcTest(FileRestController.class)
public class FileRestControllerTest {
//
//    private static final String FILE_BY_ID = "/api/pricecheck/files/";
//    private static final String FILE_NOT_FOUND_JSON = "file/template/json/fileNotFound.json";
//    private static final byte[] BYTES = new byte[]{1, 2, 3};
//    private final TemplateResolver resolver = new JsonTemplateResolver();
//    private final FileReader fileReader = new FileReader();
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private FileService fileService;
//    @Mock
//    private File file;
//
//    @Test
//    void shouldReturnBytesWhenFileExist() throws Exception {
//        long fileId = 1;
//        when(fileService.findById(fileId)).thenReturn(Optional.of(file));
//        when(file.getFile()).thenReturn(BYTES);
//
//        mvc.perform(get(FILE_BY_ID + fileId))
//                .andExpect(status().isOk())
//                .andExpect(content().bytes(file.getFile()));
//    }
//
//    @Test
//    void shouldReturnNotFoundWhenFileIsNotExist() throws Exception {
//        long fileId = -1;
//        when(fileService.findById(fileId)).thenReturn(Optional.empty());
//
//        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
//                .put("id", fileId)
//                .build();
//        String jsonContent = resolver.resolve(fileReader.read(FILE_NOT_FOUND_JSON), parameters);
//        mvc.perform(get(FILE_BY_ID + fileId))
//                .andExpect(status().isNotFound())
//                .andExpect(content().json(jsonContent));
//    }
//
//    @Test
//    void shouldReturnOkWhenDeleteAllFilesSuccess() throws Exception {
//        mvc.perform(delete(FILE_BY_ID))
//                .andExpect(status().isOk());
//    }
}
