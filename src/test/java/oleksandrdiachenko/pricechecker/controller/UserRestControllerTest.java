package oleksandrdiachenko.pricechecker.controller;

import com.google.common.collect.ImmutableMap;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.UserData;
import oleksandrdiachenko.pricechecker.service.UserService;
import oleksandrdiachenko.pricechecker.util.FileReader;
import oleksandrdiachenko.pricechecker.util.template.JsonTemplateResolver;
import oleksandrdiachenko.pricechecker.util.template.TemplateResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserRestController.class)
public class UserRestControllerTest {

    private static final String USER_BY_ID = "/api/pricecheck/users/";
    private static final String USER_JSON = "file/template/json/user.json";
    private final TemplateResolver resolver = new JsonTemplateResolver();
    private final FileReader fileReader = new FileReader();

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnUserWhenUserExist() throws Exception {
        long userId = 1;
        User user = UserData.create();
        user.setId(1);
        when(userService.findById(userId)).thenReturn(Optional.of(user));

        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
                .put("id", userId)
                .put("nickname", user.getNickname())
                .put("password", user.getPassword())
                .put("email", user.getEmail())
                .put("phone", user.getPhone())
                .build();
        String jsonContent = resolver.resolve(fileReader.read(USER_JSON), parameters);
        mvc.perform(get(USER_BY_ID + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonContent));
    }

    @Test
    void shouldReturnNotFoundWhenFileIsNotExist() throws Exception {
        long userId = -1;
        when(userService.findById(userId)).thenReturn(Optional.empty());

        mvc.perform(get(USER_BY_ID + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUserWhenUserSaved() throws Exception {
        long userId = 1;
        User user = UserData.create();
        user.setId(userId);
        when(userService.save(user)).thenReturn(user);

        Map<String, Object> parameters = ImmutableMap.<String, Object>builder()
                .put("id", userId)
                .put("nickname", user.getNickname())
                .put("password", user.getPassword())
                .put("email", user.getEmail())
                .put("phone", user.getPhone())
                .build();
        String jsonContent = resolver.resolve(fileReader.read(USER_JSON), parameters);
        mvc.perform(post(USER_BY_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
                .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonContent));
    }
}
