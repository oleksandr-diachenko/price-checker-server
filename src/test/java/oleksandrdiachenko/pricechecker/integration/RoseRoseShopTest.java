package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.RoseRoseShop;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = roseRoseShop.getDocument("https://www.roseroseshop.com/");

        assertThat(document.children()).isNotEmpty();
    }
}
