package oleksandrdiachenko.pricechecker.model.magazine;

import oleksandrdiachenko.pricechecker.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.util.Optional.ofNullable;

/**
 * @author Alexander Diachenko
 */
public class KoreaButik extends AbstractMagazine {

    private static final String DATA_QAID = "data-qaid";
    private static final String PRODUCT_PRICE = "product_price";
    private static final String SITE_DOMAIN = "korea-butik.com";
    private static final String PRESENCE_DATA = "presence_data";
    private static final String OUT_OF_STOCK = "Нет в наличии";

    @Override
    protected String getPriceFrom(Document document) {
        return ofNullable(document.getElementsByAttributeValue(DATA_QAID, PRODUCT_PRICE).first())
                .map(Element::text)
                .map(StringUtil::formatPrice)
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    protected String getSiteDomain() {
        return SITE_DOMAIN;
    }

    @Override
    public boolean isAvailable(Document document) {
        return document.getElementsByAttributeValue(DATA_QAID, PRESENCE_DATA).stream()
                .map(Element::text)
                .noneMatch(OUT_OF_STOCK::equalsIgnoreCase);
    }
}
