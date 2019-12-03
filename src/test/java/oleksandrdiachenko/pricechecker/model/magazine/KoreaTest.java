package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class KoreaTest {

    private Magazine korea;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        korea = new Korea();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = korea.getPrice(creator.createDocumentFromFile("xml/korea/Korea_discount.xml"));
        assertEquals("380.00", price);
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = korea.getPrice(creator.createDocumentFromFile("xml/korea/Korea_normal.xml"));
        assertEquals("430.00", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = korea.getPrice(creator.createDocumentFromFile("xml/korea/Korea_outofstock.xml"));
        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = korea.getPrice(creator.createDocumentFromFile("xml/korea/Korea_notfound.xml"));
        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        assertTrue(korea.isThisWebsite("https://korea.in.ua/"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        assertFalse(korea.isThisWebsite("https://www.google.com.ua/"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        assertFalse(korea.isThisWebsite("qwe"));
    }
}
