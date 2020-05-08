package oleksandrdiachenko.pricechecker.controller;

import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.service.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(FileRestController.class)
public class FileRestControllerTest {

    public static final String FILE_BY_ID = "/api/pricecheck/file/";
    private static final byte[] BYTES = new byte[]{1, 2, 3};

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FileService fileService;
    @Mock
    private File file;

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
