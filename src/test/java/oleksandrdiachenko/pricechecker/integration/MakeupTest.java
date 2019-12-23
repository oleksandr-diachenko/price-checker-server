package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.Makeup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class MakeupTest {

    private Magazine makeup;

    @BeforeEach
    void setUp() {
        makeup = new Makeup();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = makeup.getPrice(makeup.getDocument("https://makeup.com.ua/qwe"));
        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() throws IOException {
        Document document = makeup.getDocument("https://makeup.com.ua/");
        assertThat(document.children()).isNotEmpty();
    }
}
