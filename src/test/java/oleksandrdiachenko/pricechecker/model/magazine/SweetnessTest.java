package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(price).isEqualTo("217");
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = sweetness.getPrice(creator.createDocumentFromFile("xml/sweetness/Sweetness_normal.xml"));

        assertThat(price).isEqualTo("175");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = sweetness.getPrice(creator.createDocumentFromFile("xml/sweetness/Sweetness_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        String price = sweetness.getPrice(creator.createDocumentFromFile("xml/sweetness/Sweetness_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        assertThat(sweetness.isThisWebsite("https://sweetness.com.ua")).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = sweetness.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = sweetness.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
