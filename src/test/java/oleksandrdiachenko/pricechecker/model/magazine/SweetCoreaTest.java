package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko
 */
class SweetCoreaTest {

    private Magazine sweetCorea;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        sweetCorea = new SweetCorea();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = sweetCorea.getPrice(creator.createDocumentFromFile("xml/sweetCorea/SweetCorea_normal.xml"));

        assertThat(price).isEqualTo("6.300");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = sweetCorea.getPrice(creator.createDocumentFromFile("xml/sweetCorea/SweetCorea_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        String price = sweetCorea.getPrice(creator.createDocumentFromFile("xml/sweetCorea/SweetCorea_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = sweetCorea.isThisWebsite("http://www.sweetcorea.com");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = sweetCorea.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = sweetCorea.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
