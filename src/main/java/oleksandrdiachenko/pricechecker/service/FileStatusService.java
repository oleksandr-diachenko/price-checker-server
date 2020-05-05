package oleksandrdiachenko.pricechecker.service;

import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class FileStatusService {

    private final FileStatusRepository fileStatusRepository;

    @Autowired
    public FileStatusService(FileStatusRepository fileStatusRepository) {
        this.fileStatusRepository = fileStatusRepository;
    }

    public Iterable<FileStatus> findAll() {
        Iterable<FileStatus> fileStatuses = fileStatusRepository.findAll();
        log.info("Retrieved fileStatuses {}", fileStatuses);
        return fileStatuses;
    }

    public FileStatus save(FileStatus fileStatus) {
        FileStatus savedFileStatus = fileStatusRepository.save(fileStatus);
        log.info("FileStatus {} saved", savedFileStatus);
        return savedFileStatus;
    }

    public Optional<FileStatus> findById(long id) {
        Optional<FileStatus> fileStatusOptional = fileStatusRepository.findById(id);
        fileStatusOptional.ifPresentOrElse(fileStatus -> log.info("Found fileStatus: {}", fileStatus),
                () -> log.info("FileStatus for id:{} not found", id));
        return fileStatusOptional;
    }
}
