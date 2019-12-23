package oleksandrdiachenko.pricechecker.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WorkbookUtilsTest {

    @Test
    void shouldReturnNotEmptyBytes() throws Exception {
        byte[] bytes = WorkbookUtils.getBytes(new XSSFWorkbook());

        assertThat(bytes).isNotEmpty();
    }
}
