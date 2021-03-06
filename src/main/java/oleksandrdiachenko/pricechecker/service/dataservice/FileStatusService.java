package oleksandrdiachenko.pricechecker.service.dataservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileStatusService {

    private final FileStatusRepository fileStatusRepository;

    public List<FileStatus> findAll() {
        List<FileStatus> fileStatuses = IterableUtils.toList(fileStatusRepository.findAll());
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

    public List<FileStatus> findByUser(User user) {
        List<FileStatus> fileStatuses = IterableUtils.toList(fileStatusRepository.findByUser(user));
        log.info("Retrieved for user with id {} fileStatuses {}", user.getId(), fileStatuses);
        return fileStatuses;
    }

    public Optional<FileStatus> findByFileId(long fileId) {
        Optional<FileStatus> fileStatusOptional = fileStatusRepository.findByFileId(fileId);
        fileStatusOptional.ifPresentOrElse(fileStatus -> log.info("Found fileStatus: {}", fileStatus),
                () -> log.info("FileStatus with file id:{} not found", fileId));
        return fileStatusOptional;
    }
}
