package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals("1.28", price);
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = roseRoseShop.getPrice(creator.createDocumentFromFile("xml/roseRoseShop/RoseRoseShop_normal.xml"));
        assertEquals("0.88", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = roseRoseShop.getPrice(creator.createDocumentFromFile("xml/roseRoseShop/RoseRoseShop_outofstock.xml"));
        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = roseRoseShop.getPrice(creator.createDocumentFromFile("xml/roseRoseShop/RoseRoseShop_notfound.xml"));
        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        assertTrue(roseRoseShop.isThisWebsite("https://www.roseroseshop.com"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        assertFalse(roseRoseShop.isThisWebsite("https://www.google.com.ua/"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        assertFalse(roseRoseShop.isThisWebsite("qwe"));
    }
}
