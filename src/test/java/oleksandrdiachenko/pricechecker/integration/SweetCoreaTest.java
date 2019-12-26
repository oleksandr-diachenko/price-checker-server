package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.SweetCorea;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class SweetCoreaTest {

    private Magazine sweetCorea;

    @BeforeEach
    void setUp() {
        sweetCorea = new SweetCorea();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = sweetCorea.getPrice(sweetCorea.getDocument("http://www.sweetcorea.com/qwe"));

        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = sweetCorea.getDocument("http://www.sweetcorea.com");

        assertThat(document.children()).isNotEmpty();
    }
}
