package oleksandrdiachenko.pricechecker.service.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.containsAny;

@Service
public class ExcelFileValidator implements FileValidator {

    private List<String> excelTypes;

    @Autowired
    public ExcelFileValidator(List<String> excelTypes) {
        this.excelTypes = excelTypes;
    }

    @Override
    public boolean isValid(MultipartFile file) {
        return !file.isEmpty() && containsAny(excelTypes, file.getContentType());
    }
}
