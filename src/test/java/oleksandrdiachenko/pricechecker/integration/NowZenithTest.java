package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.NowZenith;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko.
 */
class NowZenithTest {

    private Magazine nowZenith;

    @BeforeEach
    void setUp() {
        nowZenith = new NowZenith();
    }

    @Test
    void shouldReturnPageNotFound() {
        String price = nowZenith.getPrice(nowZenith.getDocument("http://www.nowzenith.com/qwe"));

        assertThat(price).isEqualTo("Страница не найдена");
    }

    @Test
    void shouldReturnNotEmptyDocument() {
        Document document = nowZenith.getDocument("http://www.nowzenith.com/");

        assertThat(document.children()).isNotEmpty();
    }
}
