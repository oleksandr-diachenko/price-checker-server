package oleksandrdiachenko.pricechecker.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class WorkbookHelperTest {

    @InjectMocks
    private WorkbookHelper workbookHelper;

    @Test
    void shouldReturnNotEmptyBytes() {
        byte[] bytes = workbookHelper.getBytes(new XSSFWorkbook());

        assertThat(bytes).isNotEmpty();
    }
}
