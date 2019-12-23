package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Korea;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class KoreaButikTest {

    private Magazine korea;

    @BeforeEach
    void setUp() {
        korea = new Korea();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = korea.getPrice(korea.getDocument("https://korea-butik.com/qwe"));
        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = korea.getDocument("https://korea-butik.com");
        assertThat(document.children()).isNotEmpty();
    }
}
