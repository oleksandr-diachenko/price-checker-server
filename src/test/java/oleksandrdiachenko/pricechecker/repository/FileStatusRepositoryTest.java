package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FileStatusRepositoryTest {

    @Autowired
    private FileStatusRepository fileStatusRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnEmptyListWhenTableIsEmpty() {
        Iterable<FileStatus> fileStatuses = fileStatusRepository.findAll();

        assertThat(fileStatuses).isEmpty();
    }

    @Test
    void shouldReturnListWithTwoRecordsWhenTableHaveTwoRecords() {
        User user = entityManager.persistAndFlush(UserData.get());
        entityManager.persistAndFlush(createFileStatus(user, "fileName.xls", Status.ACCEPTED, 1,
                LocalDateTime.of(2020, 1, 6, 5, 18, 20)));
        entityManager.persistAndFlush(createFileStatus(user,
                "fileName.xlsx", Status.COMPLETED, 2,
                LocalDateTime.of(2019, 3, 4, 2, 39, 33)));

        Iterable<FileStatus> fileStatuses = fileStatusRepository.findAll();

        assertThat(fileStatuses).isNotEmpty().hasSize(2);
    }

    @Test
    void shouldReturnListOfFileStatusesByUserWhenDatabaseHaveOne() {
        User user = entityManager.persistAndFlush(UserData.get());
        entityManager.persistAndFlush(createFileStatus(user, "fileName.xls", Status.ACCEPTED, 1,
                LocalDateTime.of(2020, 1, 6, 5, 18, 20)));

        Iterable<FileStatus> fileStatuses = fileStatusRepository.findByUser(user);

        assertThat(fileStatuses).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListOfFileStatusByUserWhenDatabaseDoesntHaveOne() {
        User user = entityManager.persistAndFlush(UserData.get());

        Iterable<FileStatus> fileStatuses = fileStatusRepository.findByUser(user);

        assertThat(fileStatuses).isEmpty();
    }

    private FileStatus createFileStatus(User user, String name, Status status, long fileId, LocalDateTime localDateTime) {
        FileStatus fileStatus = new FileStatus();
        fileStatus.setName(name);
        fileStatus.setStatus(status.name());
        fileStatus.setFileId(fileId);
        fileStatus.setAcceptedTime(localDateTime);
        fileStatus.setUser(user);
        return fileStatus;
    }
}
