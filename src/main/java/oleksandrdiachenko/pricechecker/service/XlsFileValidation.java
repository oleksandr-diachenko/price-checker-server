package oleksandrdiachenko.pricechecker.service;

import org.springframework.web.multipart.MultipartFile;

public class XlsFileValidation implements FileValidator {

    @Override
    public boolean isValid(MultipartFile file) {
        return !file.isEmpty()
                && file.getSize() > 0
                && (file.getContentType().toLowerCase().equals("application/vnd.ms-excel")
                || file.getContentType().toLowerCase().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

    }
}
