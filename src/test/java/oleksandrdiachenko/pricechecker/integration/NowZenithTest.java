package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import oleksandrdiachenko.pricechecker.model.magazine.NowZenith;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        assertEquals("Страница не найдена", price);
    }

    @Test
    void shouldReturnNotEmptyDocument() throws IOException {
        Document document = nowZenith.getDocument("http://www.nowzenith.com/");
        assertFalse(document.children().isEmpty());
    }
}
