package oleksandrdiachenko.pricechecker.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkbookUtilsTest {

    @Test
    void shouldReturnNotEmptyBytes() throws Exception {
        byte[] bytes = WorkbookUtils.getBytes(new XSSFWorkbook());

        assertTrue(isNotEmpty(bytes));
    }
}
