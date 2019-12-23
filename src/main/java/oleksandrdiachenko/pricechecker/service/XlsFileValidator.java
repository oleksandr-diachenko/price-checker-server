package oleksandrdiachenko.pricechecker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.containsAny;

public class XlsFileValidator implements FileValidator {

    private List<String> excelTypes;

    @Autowired
    public XlsFileValidator(List<String> excelTypes) {
        this.excelTypes = excelTypes;
    }

    @Override
    public boolean isValid(MultipartFile file) {
        return !file.isEmpty() && containsAny(excelTypes, file.getContentType());
    }
}
