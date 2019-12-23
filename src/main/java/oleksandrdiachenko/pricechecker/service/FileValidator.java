package oleksandrdiachenko.pricechecker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileValidator {

    boolean isValid(MultipartFile file);
}
