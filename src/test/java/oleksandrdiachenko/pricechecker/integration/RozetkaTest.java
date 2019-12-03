package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.Rozetka;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        assertEquals("Страница не найдена", price);
    }

    @Test
    void shouldReturnNotEmptyDocument() throws IOException {
        Document document = rozetka.getDocument("https://rozetka.com.ua/");
        assertFalse(document.children().isEmpty());
    }
}
