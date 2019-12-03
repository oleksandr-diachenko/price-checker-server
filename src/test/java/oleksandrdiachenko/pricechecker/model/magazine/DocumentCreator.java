package oleksandrdiachenko.pricechecker.model.magazine;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Alexander Diachenko
 */
public class DocumentCreator {

    public Document createDocumentFromFile(String path) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        try {
            return Jsoup.parse(is, "UTF-8", StringUtils.EMPTY);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
