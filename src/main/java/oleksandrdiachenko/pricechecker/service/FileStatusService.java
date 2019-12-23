package oleksandrdiachenko.pricechecker.service;

import oleksandrdiachenko.pricechecker.repository.FileStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileStatusService {

    @Autowired
    private FileStatusRepository fileStatusRepository;
}
