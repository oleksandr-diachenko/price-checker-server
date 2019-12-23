package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.BeautyNetKorea;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class BeautyNetKoreaTest {

    private Magazine beautyNetKorea;

    @BeforeEach
    void setUp() {
        beautyNetKorea = new BeautyNetKorea();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = beautyNetKorea.getPrice(beautyNetKorea.getDocument("https://beautynetkorea.com/qwe"));
        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = beautyNetKorea.getDocument("https://beautynetkorea.com/");
        assertThat(document.children()).isNotEmpty();
    }
}
