package oleksandrdiachenko.pricechecker.util;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Alexander Diachenko.
 */
class UrlUtilsTest {

    @Test
    void shouldReturnTrueWhenUrlIsValid() {
        boolean isValid = UrlUtils.isValid("http://www.google.com.ua/");

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseWhenUrlIsInvalid() {
        boolean isValid = UrlUtils.isValid("qwe");

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnDomainNameWhenUrlIsValid() throws Exception {
        String domainName = UrlUtils.getDomainName("https://www.google.com.ua/search?source=hp&ei=3ORlWtPWCYSfsAH2iJ3QCQ&q=qwe&oq=qwe&gs_l=psy-ab.3...2008.2301.0.2493.0.0.0.0.0.0.0.0..0.0....0...1c.1.64.psy-ab..0.0.0....0.hNBqxwRARLg");

        assertThat(domainName).isEqualTo("www.google.com.ua");
    }

    @Test
    void shouldThrowExceptionWhenUrlIsInvalid() throws MalformedURLException {
        assertThatThrownBy(() -> UrlUtils.getDomainName("qwe"))
                .isInstanceOf(MalformedURLException.class)
                .hasMessage("no protocol: qwe");
    }
}
