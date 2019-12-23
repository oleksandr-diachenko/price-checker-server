package oleksandrdiachenko.pricechecker.model.magazine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko
 */
class KoreaButikTest {

    private Magazine koreaButik;
    private DocumentCreator creator;

    @BeforeEach
    void setup() {
        koreaButik = new KoreaButik();
        creator = new DocumentCreator();
    }

    @Test
    void shouldReturnDiscountPrice() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_discount.xml"));

        assertThat(price).isEqualTo("337.50");
    }

    @Test
    void shouldReturnNormalPrice() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_normal.xml"));

        assertThat(price).isEqualTo("145");
    }

    @Test
    void shouldReturnOutOfStock() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_outofstock.xml"));

        assertThat(price).isEqualTo("Нет в наличии");
    }

    @Test
    void shouldReturnNotFound() {
        String price = koreaButik.getPrice(creator.createDocumentFromFile("xml/koreaButik/KoreaButik_notfound.xml"));

        assertThat(price).isEqualTo("Не найдено");
    }

    @Test
    void shouldReturnTrueWhenIsThisWebSiteCalled() {
        boolean isThisWebsite = koreaButik.isThisWebsite("https://korea-butik.com");

        assertThat(isThisWebsite).isTrue();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithGoogleDomain() {
        boolean isThisWebsite = koreaButik.isThisWebsite("https://www.google.com.ua/");

        assertThat(isThisWebsite).isFalse();
    }

    @Test
    void shouldReturnFalseWhenIsThisWebSiteCalledWithIncorrectDomain() {
        boolean isThisWebsite = koreaButik.isThisWebsite("qwe");

        assertThat(isThisWebsite).isFalse();
    }
}
