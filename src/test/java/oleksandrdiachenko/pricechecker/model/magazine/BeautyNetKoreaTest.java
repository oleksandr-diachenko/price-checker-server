package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class BeautyNetKoreaTest {

    private Magazine beautyNetKorea;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        beautyNetKorea = new BeautyNetKorea();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_discount.xml"));

        assertEquals("4.47", price);
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_normal.xml"));

        assertEquals("1.43", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_outofstock.xml"));

        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_notfound.xml"));

        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = beautyNetKorea.isThisWebsite("https://beautynetkorea.com");

        assertTrue(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = beautyNetKorea.isThisWebsite("https://www.google.com.ua/");

        assertFalse(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = beautyNetKorea.isThisWebsite("qwe");

        assertFalse(isThisWebsite);
    }
}
