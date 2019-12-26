package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.Rozetka;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class RozetkaTest {

    private Magazine rozetka;

    @BeforeEach
    void setUp() {
        rozetka = new Rozetka();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = rozetka.getPrice(rozetka.getDocument("https://rozetka.com.ua/qwe"));

        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = rozetka.getDocument("https://rozetka.com.ua/");

        assertThat(document.children()).isNotEmpty();
    }
}
