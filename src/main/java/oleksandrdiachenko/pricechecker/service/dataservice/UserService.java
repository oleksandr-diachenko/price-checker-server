package oleksandrdiachenko.pricechecker.service.dataservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.Setting;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.UserRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SettingService settingService;

    public User save(User user) {
        User savedUser = userRepository.save(user);
        Setting settingByUser = settingService.findByUser(savedUser);
        if(settingByUser == null) {
            Setting setting = new Setting();
            setting.setUser(savedUser);
            settingService.save(setting);
        }
        log.info("User {} saved", savedUser);
        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        userOptional.ifPresentOrElse(user -> log.info("Found user: {}", user),
                () -> log.info("User with username:{} not found", username));
        return userOptional;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresentOrElse(user -> log.info("Found user: {}", user),
                () -> log.info("User with id:{} not found", id));
        return userOptional;
    }

    public List<User> findAll() {
        List<User> users = IterableUtils.toList(userRepository.findAll());
        log.info("Retrieved users {}", users);
        return users;
    }
}
