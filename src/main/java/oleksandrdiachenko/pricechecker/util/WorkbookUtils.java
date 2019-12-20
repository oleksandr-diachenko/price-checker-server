package oleksandrdiachenko.pricechecker.util;

import org.apache.poi.ss.usermodel.Workbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WorkbookUtils {

    public WorkbookUtils() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static byte[] getBytes(Workbook workbook) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        return os.toByteArray();
    }
}
