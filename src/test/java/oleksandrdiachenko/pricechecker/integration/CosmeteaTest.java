package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Cosmetea;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class CosmeteaTest {

    private Magazine korea;

    @BeforeEach
    void setUp() {
        korea = new Cosmetea();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = korea.getPrice(korea.getDocument("https://cosmetea.com.ua/qwe"));

        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = korea.getDocument("https://cosmetea.com.ua/");

        assertThat(document.children()).isNotEmpty();
    }
}
