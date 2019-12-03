package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.RoseRoseShop;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Alexander Diachenko.
 */
class RoseRoseShopTest {

    private Magazine roseRoseShop;

    @BeforeEach
    void setUp() {
        roseRoseShop = new RoseRoseShop();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = roseRoseShop.getPrice(roseRoseShop.getDocument("https://www.roseroseshop.com/qwe"));
        assertEquals("Страница не найдена", price);
    }

    @Test
    void shouldReturnNotEmptyDocument() throws IOException {
        Document document = roseRoseShop.getDocument("https://www.roseroseshop.com/");
        assertFalse(document.children().isEmpty());
    }
}
