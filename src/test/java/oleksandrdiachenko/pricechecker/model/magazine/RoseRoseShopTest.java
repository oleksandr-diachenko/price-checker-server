package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko
 */
class RoseRoseShopTest {

    private Magazine roseRoseShop;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        roseRoseShop = new RoseRoseShop();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = roseRoseShop.getPrice(creator.createDocumentFromFile("xml/roseRoseShop/RoseRoseShop_discount.xml"));

        assertThat(price).isEqualTo("1.28");
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = roseRoseShop.getPrice(creator.createDocumentFromFile("xml/roseRoseShop/RoseRoseShop_normal.xml"));

        assertThat(price).isEqualTo("0.88");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = roseRoseShop.getPrice(creator.createDocumentFromFile("xml/roseRoseShop/RoseRoseShop_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        String price = roseRoseShop.getPrice(creator.createDocumentFromFile("xml/roseRoseShop/RoseRoseShop_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = roseRoseShop.isThisWebsite("https://www.roseroseshop.com");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = roseRoseShop.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = roseRoseShop.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
