package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko
 */
class MakeupTest {

    private Makeup makeup;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        makeup = new Makeup();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        makeup.url = "https://makeup.com.ua/product/632463/#/option/1558727/";
        String price = makeup.getPrice(creator.createDocumentFromFile("xml/makeup/Makeup_discount.xml"));

        assertThat(price).isEqualTo("209");
    }

    @Test
    void shouldReturnNormalPrice() {
        makeup.url = "https://makeup.com.ua/product/1801/#/option/393587/";
        String price = makeup.getPrice(creator.createDocumentFromFile("xml/makeup/Makeup_normal.xml"));

        assertThat(price).isEqualTo("218");
    }

    @Test
    void shouldReturnOutOfStock() {
        makeup.url = "https://makeup.com.ua/product/20652/";
        String price = makeup.getPrice(creator.createDocumentFromFile("xml/makeup/Makeup_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        makeup.url = "https://makeup.com.ua/product/20652/";
        String price = makeup.getPrice(creator.createDocumentFromFile("xml/makeup/Makeup_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = makeup.isThisWebsite("https://makeup.com.ua/");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = makeup.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = makeup.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
