package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(price).isEqualTo("631");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = rozetka.getPrice(creator.createDocumentFromFile("xml/rozetka/Rozetka_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnSelectedProductDiscountPrice() {
        String price = rozetka.getPrice(creator.createDocumentFromFile("xml/rozetka/Rozetka_select.xml"));

        assertThat(price).isEqualTo("75");
    }

    @Test
    void shouldReturnNotFound() {
        String price = rozetka.getPrice(creator.createDocumentFromFile("xml/rozetka/Rozetka_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = rozetka.isThisWebsite("https://rozetka.com.ua");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = rozetka.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = rozetka.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
