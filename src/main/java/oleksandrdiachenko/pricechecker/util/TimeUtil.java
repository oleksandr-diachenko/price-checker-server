package oleksandrdiachenko.pricechecker.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Alexander Diachenko.
 */
public class TimeUtil {

    private TimeUtil() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }
}
