package oleksandrdiachenko.pricechecker.util;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexander Diachenko.
 */
class UrlUtilsTest {

    @Test
    void shouldReturnTrueWhenUrlIsValid() {
        boolean isValid = UrlUtils.isValid("http://www.google.com.ua/");

        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseWhenUrlIsInvalid() {
        boolean isValid = UrlUtils.isValid("qwe");

        assertFalse(isValid);
    }

    @Test
    void shouldReturnDomainNameWhenUrlIsValid() throws Exception {
        String domainName = UrlUtils.getDomainName("https://www.google.com.ua/search?source=hp&ei=3ORlWtPWCYSfsAH2iJ3QCQ&q=qwe&oq=qwe&gs_l=psy-ab.3...2008.2301.0.2493.0.0.0.0.0.0.0.0..0.0....0...1c.1.64.psy-ab..0.0.0....0.hNBqxwRARLg");

        assertEquals("www.google.com.ua", domainName);
    }

    @Test
    void shouldThrowExceptionWhenUrlIsInvalid() {
        assertThrows(MalformedURLException.class, () -> UrlUtils.getDomainName("qwe"));
    }
}
