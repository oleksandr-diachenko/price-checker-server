package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnEmptyListWhenTableIsEmpty() {
        Iterable<User> users = userRepository.findAll();

        assertThat(users).isEmpty();
    }

    @Test
    void shouldReturnListWithTwoRecordsWhenTableHaveTwoRecords() {
        entityManager.persistAndFlush(UserData.create());
        entityManager.persistAndFlush(UserData.create());

        Iterable<User> users = userRepository.findAll();

        assertThat(users).isNotEmpty().hasSize(2);
    }
}
