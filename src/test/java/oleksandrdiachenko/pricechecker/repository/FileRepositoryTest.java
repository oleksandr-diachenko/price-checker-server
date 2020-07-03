package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnEmptyListWhenTableIsEmpty() {
        Iterable<File> files = fileRepository.findAll();

        assertThat(files).isEmpty();
    }

    @Test
    void shouldReturnListWithTwoRecordsWhenTableHaveTwoRecords() {
        entityManager.persistAndFlush(createFile());
        entityManager.persistAndFlush(createFile());

        Iterable<File> files = fileRepository.findAll();

        assertThat(files).isNotEmpty().hasSize(2);
    }

    private File createFile() {
        User user = entityManager.persistAndFlush(UserData.get());
        File file = new File();
        file.setFile(new byte[]{1, 2, 3});
        file.setUser(user);
        return file;
    }
}
