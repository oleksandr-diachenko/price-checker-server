package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Korea;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class KoreaTest {

    private Magazine korea;

    @BeforeEach
    void setUp() {
        korea = new Korea();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = korea.getPrice(korea.getDocument("https://korea.in.ua/qwe"));

        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = korea.getDocument("https://korea.in.ua/");

        assertThat(document.children()).isNotEmpty();
    }
}
