package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Status;
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
        entityManager.persistAndFlush(createFileStatus("fileName.xls", Status.ACCEPTED, 1,
                LocalDateTime.of(2020, 1, 6, 5, 18, 20)));
        entityManager.persistAndFlush(createFileStatus("fileName.xlsx", Status.COMPLETED, 2,
                LocalDateTime.of(2019, 3, 4, 2, 39, 33)));

        Iterable<FileStatus> fileStatuses = fileStatusRepository.findAll();

        assertThat(fileStatuses).isNotEmpty().hasSize(2);
    }

    private FileStatus createFileStatus(String name, Status status, long fileId, LocalDateTime localDateTime) {
        FileStatus fileStatus = new FileStatus();
        fileStatus.setName(name);
        fileStatus.setStatus(status.name());
        fileStatus.setFileId(fileId);
        fileStatus.setAcceptedTime(localDateTime);
        return fileStatus;
    }
}
