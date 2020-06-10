package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void shouldReturnUserWhenRepositoryReturnUser() {
        String username = "POSITIV";
        User user = new User();
        when(userRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.findByUsername(username);

        assertThat(userOptional).containsSame(user);
    }

    @Test
    void shouldReturnEmptyOptionalWhenRepositoryReturnEmptyOptional() {
        String username = "POSITIV";
        when(userRepository.findByUsernameIgnoreCase(username)).thenReturn(Optional.empty());

        Optional<User> userOptional = userService.findByUsername(username);

        assertThat(userOptional).isEmpty();
    }

    @Test
    void shouldCallSaveToRepositoryWhenSaveExecuted() {
        User user = new User();

        userService.save(user);

        verify(userRepository).save(user);
    }
}
