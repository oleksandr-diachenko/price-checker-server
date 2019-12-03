package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class RozetkaTest {

    private Magazine rozetka;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        rozetka = new Rozetka();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = rozetka.getPrice(creator.createDocumentFromFile("xml/rozetka/Rozetka_discount.xml"));
        assertEquals("631", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = rozetka.getPrice(creator.createDocumentFromFile("xml/rozetka/Rozetka_outofstock.xml"));
        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnSelectedProductDiscountPrice() {
        String price = rozetka.getPrice(creator.createDocumentFromFile("xml/rozetka/Rozetka_select.xml"));
        assertEquals("75", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = rozetka.getPrice(creator.createDocumentFromFile("xml/rozetka/Rozetka_notfound.xml"));
        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        assertTrue(rozetka.isThisWebsite("https://rozetka.com.ua"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        assertFalse(rozetka.isThisWebsite("https://www.google.com.ua/"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        assertFalse(rozetka.isThisWebsite("qwe"));
    }
}
