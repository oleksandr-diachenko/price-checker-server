package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class SweetCoreaTest {

    private Magazine sweetCorea;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        sweetCorea = new SweetCorea();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = sweetCorea.getPrice(creator.createDocumentFromFile("xml/sweetCorea/SweetCorea_normal.xml"));

        assertEquals("6.300", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = sweetCorea.getPrice(creator.createDocumentFromFile("xml/sweetCorea/SweetCorea_outofstock.xml"));

        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = sweetCorea.getPrice(creator.createDocumentFromFile("xml/sweetCorea/SweetCorea_notfound.xml"));

        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = sweetCorea.isThisWebsite("http://www.sweetcorea.com");

        assertTrue(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = sweetCorea.isThisWebsite("https://www.google.com.ua/");

        assertFalse(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = sweetCorea.isThisWebsite("qwe");

        assertFalse(isThisWebsite);
    }
}
