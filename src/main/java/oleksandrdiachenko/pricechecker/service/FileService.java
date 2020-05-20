package oleksandrdiachenko.pricechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    public File save(File file) {
        File savedFile = fileRepository.save(file);
        log.info("File {} saved", savedFile);
        return savedFile;
    }

    public Optional<File> findById(long id) {
        Optional<File> fileOptional = fileRepository.findById(id);
        fileOptional.ifPresentOrElse(file -> log.info("Found file: {}", file),
                () -> log.info("File for id:{} not found", id));
        return fileOptional;
    }

    public void deleteAll() {
        fileRepository.deleteAll();
        log.info("All files deleted");
    }
}
