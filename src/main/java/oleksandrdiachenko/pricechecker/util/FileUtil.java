package oleksandrdiachenko.pricechecker.util;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Alexander Diachenko.
 */
public class FileUtil {

    private FileUtil() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    /**
     * Open file with default application.
     *
     * @param file
     * @throws IOException
     */
    public static void open(File file) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }
}
