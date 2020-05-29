package oleksandrdiachenko.pricechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        userOptional.ifPresentOrElse(user -> log.info("Found user: {}", user),
                () -> log.info("User with id:{} not found", id));
        return userOptional;
    }

    public User save(User user) {
        User savedUser = userRepository.save(user);
        log.info("User {} saved", savedUser);
        return savedUser;
    }
}
