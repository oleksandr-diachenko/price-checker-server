package oleksandrdiachenko.pricechecker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class XlsFileValidatorTest {

    @InjectMocks
    private XlsFileValidator fileValidator;

    @Mock
    private MultipartFile file;
    @Spy
    private ArrayList<String> excelTypes;

    @BeforeEach
    void setUp() {
        when(file.isEmpty()).thenReturn(false);
        excelTypes.add("application/vnd.ms-excel");
        excelTypes.add( "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Test
    void shouldReturnFalseWhenFileIsEmpty() {
        when(file.isEmpty()).thenReturn(true);

        assertThat(fileValidator.isValid(file)).isFalse();
    }

    @Test
    void shouldReturnFalseWhenFileIsJpg() {
        when(file.getContentType()).thenReturn("image/jpg");

        assertThat(fileValidator.isValid(file)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenFileIsXls() {
        when(file.getContentType()).thenReturn("application/vnd.ms-excel");

        assertThat(fileValidator.isValid(file)).isTrue();
    }

    @Test
    void shouldReturnTrueWhenFileIsXlsx() {
        when(file.getContentType()).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        assertThat(fileValidator.isValid(file)).isTrue();
    }
}
