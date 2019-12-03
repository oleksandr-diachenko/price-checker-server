package oleksandrdiachenko.pricechecker.model.magazine;

import org.jsoup.nodes.Document;

/**
 * @author Alexander Diachenko.
 */
public interface Magazine {

    String getPrice(Document document);

    boolean isThisWebsite(String url);

    Document getDocument(String url);

    boolean isAvailable(Document document);
}
