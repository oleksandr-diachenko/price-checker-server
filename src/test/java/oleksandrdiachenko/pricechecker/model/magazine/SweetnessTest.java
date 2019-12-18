package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko
 */
class SweetnessTest {

    private Magazine sweetness;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        sweetness = new Sweetness();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = sweetness.getPrice(creator.createDocumentFromFile("xml/sweetness/Sweetness_discount.xml"));

        assertEquals("217", price);
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = sweetness.getPrice(creator.createDocumentFromFile("xml/sweetness/Sweetness_normal.xml"));

        assertEquals("175", price);
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = sweetness.getPrice(creator.createDocumentFromFile("xml/sweetness/Sweetness_outofstock.xml"));

        assertEquals("Нет в наличии", price);
    }

    @Test
    void shouldReturnNotFound() {
        String price = sweetness.getPrice(creator.createDocumentFromFile("xml/sweetness/Sweetness_notfound.xml"));

        assertEquals("Не найдено", price);
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        assertTrue(sweetness.isThisWebsite("https://sweetness.com.ua"));
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = sweetness.isThisWebsite("https://www.google.com.ua/");

        assertFalse(isThisWebsite);
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = sweetness.isThisWebsite("qwe");

        assertFalse(isThisWebsite);
    }
}
