package oleksandrdiachenko.pricechecker.service.dataservice;

import oleksandrdiachenko.pricechecker.model.entity.Setting;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.UserData;
import oleksandrdiachenko.pricechecker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String USERNAME = "POSITIV";
    private static final String EMAIL = "mail@mail.com";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private SettingService settingService;

    @Test
    void shouldReturnUserWhenRepositoryReturnUser() {
        User user = new User();
        when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.findByUsername(USERNAME);

        assertThat(userOptional).containsSame(user);
    }

    @Test
    void shouldReturnEmptyOptionalWhenRepositoryReturnEmptyOptional() {
        when(userRepository.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.empty());

        Optional<User> userOptional = userService.findByUsername(USERNAME);

        assertThat(userOptional).isEmpty();
    }

    @Test
    void shouldNotUpdateSettingsWhenSettingsAlreadyPresent() {
        User user = new User();
        User savedUser = new User();
        when(userRepository.save(user)).thenReturn(savedUser);
        Setting setting = new Setting();
        when(settingService.findByUser(savedUser)).thenReturn(setting);

        userService.save(user);

        verify(userRepository).save(user);
        verify(settingService, never()).save(any(Setting.class));
    }

    @Test
    void shouldReturnTrueByUserNameWhenRepositoryReturnTrue() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThat(userService.existsByUsername(USERNAME)).isTrue();
    }

    @Test
    void shouldReturnFalseByUserNameWhenRepositoryReturnFalse() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);

        assertThat(userService.existsByUsername(USERNAME)).isFalse();
    }

    @Test
    void shouldReturnTrueByEmailWhenRepositoryReturnTrue() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);

        assertThat(userService.existsByEmail(EMAIL)).isTrue();
    }

    @Test
    void shouldReturnFalseByEmailWhenRepositoryReturnFalse() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);

        assertThat(userService.existsByEmail(EMAIL)).isFalse();
    }

    @Test
    void shouldSaveNewSettingsWhenSettingsNotPresentPresent() {
        User user = new User();
        User savedUser = new User();
        when(userRepository.save(user)).thenReturn(savedUser);
        when(settingService.findByUser(savedUser)).thenReturn(null);
        Setting setting = new Setting();
        setting.setUser(savedUser);

        userService.save(user);

        verify(userRepository).save(user);
        verify(settingService).save(setting);
    }

    @Test
    void shouldReturnListOfUsersWhenRepositoryReturnOne() {
        when(userRepository.findAll()).thenReturn(List.of(UserData.get()));

        List<User> users = userService.findAll();

        assertThat(users).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListOfUsersWhenRepositoryReturnEmptyOne() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> users = userService.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    void shouldReturnOptionalUserWhenRepositoryReturnOne() {
        long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(UserData.get()));

        Optional<User> optionalUser = userService.findById(id);

        assertThat(optionalUser).isPresent();
    }

    @Test
    void shouldReturnOptionalEmptyWhenRepositoryDoesntReturnOne() {
        long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> optionalUser = userService.findById(id);

        assertThat(optionalUser).isNotPresent();
    }
}
