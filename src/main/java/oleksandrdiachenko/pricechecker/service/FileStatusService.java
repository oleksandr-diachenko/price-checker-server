package oleksandrdiachenko.pricechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileStatusService {

    private final FileStatusRepository fileStatusRepository;

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
                () -> log.info("FileStatus with id:{} not found", id));
        return fileStatusOptional;
    }

    public void deleteAll() {
        fileStatusRepository.deleteAll();
        log.info("All file statuses deleted");
    }

    public Iterable<FileStatus> findByUserId(User user) {
        Iterable<FileStatus> fileStatuses = fileStatusRepository.findByUser(user);
        log.info("Retrieved for user with id {} fileStatuses {}", user.getId(), fileStatuses);
        return fileStatuses;
    }
}
