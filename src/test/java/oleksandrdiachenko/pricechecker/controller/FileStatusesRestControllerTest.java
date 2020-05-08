package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FileStatusesRestController.class)
public class FileStatusesRestControllerTest {

    public static final String FILESTATUSES = "/api/pricecheck/filestatuses";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileStatusService fileStatusService;

    @Test
    void shouldReturnListWithOneFileStatusesWhenServiceReturnListWithOneFileStatus() throws Exception {
        when(fileStatusService.findAll()).thenReturn(Collections.singletonList(
                new FileStatus(1, "filename", Status.COMPLETED.name(), 2,
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
    void shouldReturnOkWhenDeleteAllFilesSuccess() throws Exception {
        mvc.perform(delete(FILESTATUSES))
                .andExpect(status().isOk());
    }
}
