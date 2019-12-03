package oleksandrdiachenko.pricechecker.util;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexander Diachenko
 */
class StringUtilTest {

    @Test
    void shouldReturnSimpleNumberWhenPassedSimpleNumber() {
        String formatted = StringUtil.formatPrice("1");
        assertEquals("1", formatted);
    }

    @Test
    void shouldReturnSimpleNumberWhenPassedSimpleString() {
        String formatted = StringUtil.formatPrice("a1b");
        assertEquals("1", formatted);
    }

    @Test
    void shouldReturnSimpleNumberWhenPassedStringWithSpaces() {
        String formatted = StringUtil.formatPrice("a 1 b");
        assertEquals("1", formatted);
    }

    @Test
    void shouldReturnDecimalWhenPassedMixedString() {
        String formatted = StringUtil.formatPrice("a 1 b.z 2");
        assertEquals("1.2", formatted);
    }

    @Test
    void shouldReturnDecimalWhenPassedMixedStringWithEndingDot() {
        String formatted = StringUtil.formatPrice("aa 345 ff.");
        assertEquals("345", formatted);
    }

    @Test
    void shouldReturnDecimalWhenPassedMixedStringWithLeadingDot() {
        String formatted = StringUtil.formatPrice("aa. 345 ff");
        assertEquals("345", formatted);
    }
}
