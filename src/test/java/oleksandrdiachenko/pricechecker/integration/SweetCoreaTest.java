package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.SweetCorea;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Alexander Diachenko.
 */
class SweetCoreaTest {

    private Magazine sweetCorea;

    @BeforeEach
    void setUp() {
        sweetCorea = new SweetCorea();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = sweetCorea.getPrice(sweetCorea.getDocument("http://www.sweetcorea.com/qwe"));
        assertEquals("Страница не найдена", price);
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = sweetCorea.getDocument("http://www.sweetcorea.com");
        assertFalse(document.children().isEmpty());
    }
}
