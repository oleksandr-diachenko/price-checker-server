package oleksandrdiachenko.pricechecker.controller;

import com.google.common.collect.ImmutableMap;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.util.FileReader;
import oleksandrdiachenko.pricechecker.util.template.JsonTemplateResolver;
import oleksandrdiachenko.pricechecker.util.template.TemplateResolver;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Ignore //TODO find how test secure methods
@ExtendWith(SpringExtension.class)
@WebMvcTest(FileStatusesRestController.class)
public class FileStatusesRestControllerTest {
//
//    private static final String FILESTATUSES = "/api/pricecheck/filestatuses";
//    private static final String FILE_STATUSES_JSON = "file/template/json/fileStatuses.json";
//    private final TemplateResolver resolver = new JsonTemplateResolver();
//    private final FileReader fileReader = new FileReader();
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private FileStatusService fileStatusService;
//
//    @Test
//    void shouldReturnListWithOneFileStatusesWhenServiceReturnListWithOneFileStatus() throws Exception {
//        int id = 1;
//        String filename = "filename";
//        int fileId = 2;
//        LocalDateTime acceptedTime = LocalDateTime.of(2020, 1, 6, 5, 18, 20);
//        String status = Status.COMPLETED.name();
//        when(fileStatusService.findAll()).thenReturn(Collections.singletonList(
//                new FileStatus(id, filename, status, fileId, acceptedTime, new User())));
//
//        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
//                .put("id", id)
//                .put("name", filename)
//                .put("status", status)
//                .put("fileId", fileId)
//                .put("acceptedTime", acceptedTime.toString())
//                .build();
//        String jsonContent = resolver.resolve(fileReader.read(FILE_STATUSES_JSON), parameters);
//        mvc.perform(get(FILESTATUSES))
//                .andExpect(status().isOk())
//                .andExpect(content().json(jsonContent));
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenServiceReturnEmptyList() throws Exception {
//        when(fileStatusService.findAll()).thenReturn(Collections.emptyList());
//
//        mvc.perform(get(FILESTATUSES))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//    }
//
//    @Test
//    void shouldReturnOkWhenDeleteAllFilesSuccess() throws Exception {
//        mvc.perform(delete(FILESTATUSES))
//                .andExpect(status().isOk());
//    }
}
