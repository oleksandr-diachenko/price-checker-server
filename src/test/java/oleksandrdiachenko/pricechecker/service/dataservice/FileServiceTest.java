package oleksandrdiachenko.pricechecker.service.dataservice;

import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import oleksandrdiachenko.pricechecker.service.dataservice.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    @Test
    void shouldReturnFileWhenRepositoryReturnFile() {
        long id = 1;
        File file = new File();
        when(fileRepository.findById(id)).thenReturn(Optional.of(file));

        Optional<File> fileOptional = fileService.findById(id);

        assertThat(fileOptional).containsSame(file);
    }

    @Test
    void shouldReturnEmptyOptionalWhenRepositoryReturnEmptyOptional() {
        long id = 1;
        when(fileRepository.findById(id)).thenReturn(Optional.empty());

        Optional<File> fileOptional = fileService.findById(id);

        assertThat(fileOptional).isEmpty();
    }

    @Test
    void shouldCallSaveToRepositoryWhenSaveExecuted() {
        File file = new File();

        fileService.save(file);

        verify(fileRepository).save(file);
    }

    @Test
    void shouldCallDeleteAllWhenDeleteAllExecuted() {
        fileService.deleteAll();

        verify(fileRepository).deleteAll();
    }
}
