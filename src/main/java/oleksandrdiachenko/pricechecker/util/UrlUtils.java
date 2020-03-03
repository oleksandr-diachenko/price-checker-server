package oleksandrdiachenko.pricechecker.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Alexander Diachenko.
 */
@Slf4j
public class UrlUtils {

    private UrlUtils() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static String getDomainName(String url) throws MalformedURLException {
        String domainName = new URL(url).getHost();
        log.info("For url: [{}] domain name is: [{}]", url, domainName);
        return domainName;
    }

    public static boolean isValid(String url) {
        String[] schemes = {"http","https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        boolean isValid = urlValidator.isValid(url);
        log.info("Url: [{}] is valid: [{}]", url, isValid);
        return isValid;
    }
}
