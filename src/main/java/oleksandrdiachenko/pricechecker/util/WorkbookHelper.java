package oleksandrdiachenko.pricechecker.util;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Log4j2
@Component
public class WorkbookHelper {

    public byte[] getBytes(Workbook workbook) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            workbook.write(os);
            return os.toByteArray();
        } catch (IOException e) {
            log.error("Can't read bytes from workbook!");
            throw new RuntimeException("Can't read bytes from workbook!", e);
        }
    }
}
