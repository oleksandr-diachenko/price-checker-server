package oleksandrdiachenko.pricechecker.util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * @author Alexander Diachenko.
 */
public class AppProperty {

    private static final Logger logger = Logger.getLogger(AppProperty.class);

    private AppProperty() {
        throw new IllegalStateException("Creating object not allowed!");
    }

    public static Properties getProperty() {
        Properties mainProperties = new Properties();
        FileInputStream file;
        String path = "./config.properties";
        try {
            file = new FileInputStream(path);
            mainProperties.load(file);
            file.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return mainProperties;
    }

    public static Properties setProperties(Properties properties) {
        try (OutputStream output = new FileOutputStream("./config.properties")) {
            properties.store(output, null);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return properties;
    }
}
