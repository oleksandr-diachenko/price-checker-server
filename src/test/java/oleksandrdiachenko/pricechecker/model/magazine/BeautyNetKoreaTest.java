package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko
 */
class BeautyNetKoreaTest {

    private Magazine beautyNetKorea;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        beautyNetKorea = new BeautyNetKorea();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_discount.xml"));

        assertThat(price).isEqualTo("4.47");
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_normal.xml"));

        assertThat(price).isEqualTo("1.43");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        String price = beautyNetKorea.getPrice(creator.createDocumentFromFile("xml/beautyNewKorea/BeautyNetKorea_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = beautyNetKorea.isThisWebsite("https://beautynetkorea.com");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = beautyNetKorea.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = beautyNetKorea.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
