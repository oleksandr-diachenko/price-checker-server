package oleksandrdiachenko.pricechecker.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/6/2020
 **/
@ExtendWith(MockitoExtension.class)
class FileReaderTest {

    private static final String CONTENT = "one line of code";

    @InjectMocks
    private FileReader reader;

    @Test
    void shouldReturnFileBodyAsStringWhenFileExist() throws IOException {
        String content = reader.read("file/txt/reader.txt");

        assertThat(content).isEqualTo(CONTENT);
    }

    @Test
    void shouldThrowExceptionWhenFileDoesntExist() {
        assertThatIllegalArgumentException().isThrownBy(() -> reader.read("qwe"))
                .withMessage("File not found")
                .withNoCause();
    }
}