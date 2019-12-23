package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(price).isEqualTo("380.00");
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = korea.getPrice(creator.createDocumentFromFile("xml/korea/Korea_normal.xml"));

        assertThat(price).isEqualTo("430.00");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = korea.getPrice(creator.createDocumentFromFile("xml/korea/Korea_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        String price = korea.getPrice(creator.createDocumentFromFile("xml/korea/Korea_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = korea.isThisWebsite("https://korea.in.ua/");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = korea.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = korea.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
