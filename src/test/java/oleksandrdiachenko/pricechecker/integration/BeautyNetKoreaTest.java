package oleksandrdiachenko.pricechecker.integration;

import oleksandrdiachenko.pricechecker.model.magazine.BeautyNetKorea;
import oleksandrdiachenko.pricechecker.model.magazine.Magazine;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        assertEquals("Страница не найдена", price);
    }

    @Test
    void shouldReturnNotEmptyDocument() throws IOException {
        Document document = beautyNetKorea.getDocument("https://beautynetkorea.com/");
        assertFalse(document.children().isEmpty());
    }
}
