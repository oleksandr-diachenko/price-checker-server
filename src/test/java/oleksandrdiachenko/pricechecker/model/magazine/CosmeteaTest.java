package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class CosmeteaTest {

    private Magazine cosmetea;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        cosmetea = new Cosmetea();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = cosmetea.getPrice(creator.createDocumentFromFile("xml/cosmetea/Cosmetea_discount.xml"));
        assertEquals("418", price);
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = cosmetea.getPrice(creator.createDocumentFromFile("xml/cosmetea/Cosmetea_normal.xml"));
        assertEquals("250", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = cosmetea.getPrice(creator.createDocumentFromFile("xml/cosmetea/Cosmetea_outofstock.xml"));
        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = cosmetea.getPrice(creator.createDocumentFromFile("xml/cosmetea/Cosmetea_notfound.xml"));
        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        assertTrue(cosmetea.isThisWebsite("https://cosmetea.com.ua"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        assertFalse(cosmetea.isThisWebsite("https://www.google.com.ua/"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        assertFalse(cosmetea.isThisWebsite("qwe"));
    }
}
