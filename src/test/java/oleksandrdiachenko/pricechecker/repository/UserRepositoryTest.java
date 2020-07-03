package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnUserByNameIgnoreCaseWhenCaseIsDifferent() {
        entityManager.persistAndFlush(UserData.get());

        Optional<User> optionalUser = userRepository.findByUsernameIgnoreCase("positiv");

        assertThat(optionalUser).isPresent();
    }

    @Test
    void shouldReturnTrueExistByUsernameWhenDatabaseHaveOne() {
        entityManager.persistAndFlush(UserData.get());

        assertThat(userRepository.existsByUsername("POSITIV")).isTrue();
    }

    @Test
    void shouldReturnFalseExistByUsernameWhenDatabaseDoesntHaveOne() {
        entityManager.persistAndFlush(UserData.get());

        assertThat(userRepository.existsByUsername("qwe")).isFalse();
    }

    @Test
    void shouldReturnTrueExistByEmailWhenDatabaseHaveOne() {
        entityManager.persistAndFlush(UserData.get());

        assertThat(userRepository.existsByEmail("mail@mail.com")).isTrue();
    }

    @Test
    void shouldReturnFalseExistByEmailWhenDatabaseDoesntHaveOne() {
        entityManager.persistAndFlush(UserData.get());

        assertThat(userRepository.existsByUsername("qwe@mail.com")).isFalse();
    }
}
