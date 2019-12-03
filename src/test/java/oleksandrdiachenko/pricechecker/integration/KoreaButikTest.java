package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Korea;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Alexander Diachenko.
 */
class KoreaButikTest {

    private Magazine korea;

    @BeforeEach
    void setUp() {
        korea = new Korea();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = korea.getPrice(korea.getDocument("https://korea-butik.com/qwe"));
        assertEquals("Страница не найдена", price);
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = korea.getDocument("https://korea-butik.com");
        assertFalse(document.children().isEmpty());
    }
}
