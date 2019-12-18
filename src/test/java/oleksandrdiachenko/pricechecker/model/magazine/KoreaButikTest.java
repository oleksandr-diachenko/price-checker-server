package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class KoreaButikTest {

    private Magazine koreaButik;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        koreaButik = new KoreaButik();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_discount.xml"));

        assertEquals("337.50", price);
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_normal.xml"));

        assertEquals("145", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_outofstock.xml"));

        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_notfound.xml"));

        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = koreaButik.isThisWebsite("https://korea-butik.com");

        assertTrue(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = koreaButik.isThisWebsite("https://www.google.com.ua/");

        assertFalse(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = koreaButik.isThisWebsite("qwe");

        assertFalse(isThisWebsite);
    }
}
