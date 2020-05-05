package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

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
}
