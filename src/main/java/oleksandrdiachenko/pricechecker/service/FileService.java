package oleksandrdiachenko.pricechecker.service;

import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

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
}
