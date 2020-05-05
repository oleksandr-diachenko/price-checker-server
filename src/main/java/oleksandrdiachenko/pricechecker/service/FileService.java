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

    public Optional<File> getFileById(long fileId) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if(optionalFile.isEmpty()) {
            log.info("File with id {} not found", fileId);
        }
        return optionalFile;
    }
}
