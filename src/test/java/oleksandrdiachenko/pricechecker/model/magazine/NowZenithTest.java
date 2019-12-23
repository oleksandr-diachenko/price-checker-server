package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(price).isEqualTo("6.19");
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = nowZenith.getPrice(creator.createDocumentFromFile("xml/nowZenith/NowZenith_normal.xml"));

        assertThat(price).isEqualTo("4.60");
    }

    @Test
    void shouldReturnNotFound() {
        String price = nowZenith.getPrice(creator.createDocumentFromFile("xml/nowZenith/NowZenith_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = nowZenith.isThisWebsite("http://www.nowzenith.com");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = nowZenith.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = nowZenith.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
