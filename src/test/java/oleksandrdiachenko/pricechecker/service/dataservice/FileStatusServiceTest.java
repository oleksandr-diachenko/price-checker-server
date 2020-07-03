package oleksandrdiachenko.pricechecker.service.dataservice;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileStatusServiceTest {

    @InjectMocks
    private FileStatusService fileStatusService;

    @Mock
    private FileStatusRepository fileStatusRepository;

    @Test
    void shouldReturnListWhenRepositoryList() {
        FileStatus fileStatus = new FileStatus();
        when(fileStatusRepository.findAll()).thenReturn(Collections.singletonList(fileStatus));

        Iterable<FileStatus> fileStatuses = fileStatusService.findAll();

        assertThat(fileStatuses).containsOnly(fileStatus);
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryReturnEmptyList() {
        when(fileStatusRepository.findAll()).thenReturn(Collections.emptyList());

        Iterable<FileStatus> fileStatuses = fileStatusService.findAll();

        assertThat(fileStatuses).isEmpty();
    }

    @Test
    void shouldCallSaveToRepositoryWhenSaveExecuted() {
        FileStatus file = new FileStatus();

        fileStatusService.save(file);

        verify(fileStatusRepository).save(file);
    }

    @Test
    void shouldCallDeleteAllWhenDeleteAllExecuted() {
        fileStatusService.deleteAll();

        verify(fileStatusRepository).deleteAll();
    }

    @Test
    void shouldReturnFileStatusOnFindByIdWhenRepositoryReturnOne() {
        long id = 1L;
        FileStatus fileStatus = new FileStatus();
        when(fileStatusRepository.findById(id)).thenReturn(Optional.of(fileStatus));

        Optional<FileStatus> fileStatusOptional = fileStatusService.findById(id);

        assertThat(fileStatusOptional).isPresent();
    }

    @Test
    void shouldReturnEmptyOptionalOnFindByIdWhenRepositoryDoesntReturnOne() {
        long id = 1L;
        when(fileStatusRepository.findById(id)).thenReturn(Optional.empty());

        Optional<FileStatus> fileStatusOptional = fileStatusService.findById(id);

        assertThat(fileStatusOptional).isNotPresent();
    }

    @Test
    void shouldReturnListOfFileStatusOnFindByUserWhenRepositoryReturnOne() {
        User user = new User();
        FileStatus fileStatus = new FileStatus();
        when(fileStatusRepository.findByUser(user)).thenReturn(Collections.singletonList(fileStatus));

        Iterable<FileStatus> fileStatuses = fileStatusService.findByUser(user);

        assertThat(fileStatuses).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListOfFileStatusesOnFindByUserWhenRepositoryDoesntReturnOne() {
        User user = new User();
        when(fileStatusRepository.findByUser(user)).thenReturn(Collections.emptyList());

        Iterable<FileStatus> fileStatuses = fileStatusService.findByUser(user);

        assertThat(fileStatuses).isEmpty();
    }

    @Test
    void shouldReturnFileStatusOnFindByFileIdWhenRepositoryReturnOne() {
        long id = 1L;
        FileStatus fileStatus = new FileStatus();
        when(fileStatusRepository.findByFileId(id)).thenReturn(Optional.of(fileStatus));

        Optional<FileStatus> fileStatusOptional = fileStatusService.findByFileId(id);

        assertThat(fileStatusOptional).isPresent();
    }

    @Test
    void shouldReturnEmptyOptionalOnFindByFileIdWhenRepositoryDoesntReturnOne() {
        long id = 1L;
        when(fileStatusRepository.findByFileId(id)).thenReturn(Optional.empty());

        Optional<FileStatus> fileStatusOptional = fileStatusService.findByFileId(id);

        assertThat(fileStatusOptional).isNotPresent();
    }
}
