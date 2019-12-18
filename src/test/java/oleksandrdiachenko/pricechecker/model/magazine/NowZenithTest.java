package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class NowZenithTest {

    private Magazine nowZenith;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        nowZenith = new NowZenith();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = nowZenith.getPrice(creator.createDocumentFromFile("xml/nowZenith/NowZenith_discount.xml"));

        assertEquals("6.19", price);
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = nowZenith.getPrice(creator.createDocumentFromFile("xml/nowZenith/NowZenith_normal.xml"));

        assertEquals("4.60", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = nowZenith.getPrice(creator.createDocumentFromFile("xml/nowZenith/NowZenith_notfound.xml"));

        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = nowZenith.isThisWebsite("http://www.nowzenith.com");

        assertTrue(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = nowZenith.isThisWebsite("https://www.google.com.ua/");

        assertFalse(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = nowZenith.isThisWebsite("qwe");

        assertFalse(isThisWebsite);
    }
}
