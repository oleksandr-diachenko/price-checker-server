package oleksandrdiachenko.pricechecker.util;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexander Diachenko
 */
class StringUtilTest {

    @Test
    void shouldReturnSimpleNumberWhenPassedSimpleNumber() {
        String formatted = StringUtil.formatPrice("1");

        assertThat(formatted).isEqualTo("1");
    }

    @Test
    void shouldReturnSimpleNumberWhenPassedSimpleString() {
        String formatted = StringUtil.formatPrice("a1b");

        assertThat(formatted).isEqualTo("1");
    }

    @Test
    void shouldReturnSimpleNumberWhenPassedStringWithSpaces() {
        String formatted = StringUtil.formatPrice("a 1 b");

        assertThat(formatted).isEqualTo("1");
    }

    @Test
    void shouldReturnDecimalWhenPassedMixedString() {
        String formatted = StringUtil.formatPrice("a 1 b.z 2");

        assertThat(formatted).isEqualTo("1.2");
    }

    @Test
    void shouldReturnDecimalWhenPassedMixedStringWithEndingDot() {
        String formatted = StringUtil.formatPrice("aa 345 ff.");

        assertThat(formatted).isEqualTo("345");
    }

    @Test
    void shouldReturnDecimalWhenPassedMixedStringWithLeadingDot() {
        String formatted = StringUtil.formatPrice("aa. 345 ff");

        assertThat(formatted).isEqualTo("345");
    }
}
