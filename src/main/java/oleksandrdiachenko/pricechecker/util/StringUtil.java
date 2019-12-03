package oleksandrdiachenko.pricechecker.util;

/**
 * @author Alexander Diachenko
 */
public class StringUtil {

    private StringUtil() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static String formatPrice(String price) {
        String string = price
                .replaceAll("[^0-9.,]+", "")
                .replaceAll(",", ".");
        if (startWithDotOrComa(string)) {
            string = string.substring(1);
            formatPrice(string);
        }
        if (endWithDotOrComa(string)) {
            string = string.substring(0, string.length() - 1);
            formatPrice(string);
        }
        return string;
    }

    private static boolean endWithDotOrComa(String string) {
        return string.endsWith(".") || string.endsWith(",");
    }

    private static boolean startWithDotOrComa(String string) {
        return string.startsWith(".") || string.startsWith(",");
    }
}
