package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Cosmetea;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        assertEquals("Страница не найдена", price);
    }

    @Test
    void shouldReturnNotEmptyDocument() throws IOException {
        Document document = korea.getDocument("https://cosmetea.com.ua/");
        assertFalse(document.children().isEmpty());
    }
}
