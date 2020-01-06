package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileStatusService {

    private FileStatusRepository fileStatusRepository;

    @Autowired
    public FileStatusService(FileStatusRepository fileStatusRepository) {
        this.fileStatusRepository = fileStatusRepository;
    }

    public Iterable<FileStatus> getAllFileStatuses() {
        return fileStatusRepository.findAll();
    }
}
