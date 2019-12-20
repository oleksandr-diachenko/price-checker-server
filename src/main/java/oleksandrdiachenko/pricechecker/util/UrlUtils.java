package oleksandrdiachenko.pricechecker.util;

import org.apache.commons.validator.routines.UrlValidator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Alexander Diachenko.
 */
public class UrlUtils {

    private UrlUtils() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static String getDomainName(String url) throws MalformedURLException {
        return new URL(url).getHost();
    }

    public static boolean isValid(String url) {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }
}
