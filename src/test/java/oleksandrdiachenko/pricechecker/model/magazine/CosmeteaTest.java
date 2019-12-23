package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(price).isEqualTo("418");
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = cosmetea.getPrice(creator.createDocumentFromFile("xml/cosmetea/Cosmetea_normal.xml"));

        assertThat(price).isEqualTo("250");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = cosmetea.getPrice(creator.createDocumentFromFile("xml/cosmetea/Cosmetea_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        String price = cosmetea.getPrice(creator.createDocumentFromFile("xml/cosmetea/Cosmetea_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = cosmetea.isThisWebsite("https://cosmetea.com.ua");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = cosmetea.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = cosmetea.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
