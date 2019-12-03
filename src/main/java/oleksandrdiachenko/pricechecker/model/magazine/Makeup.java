package oleksandrdiachenko.pricechecker.model.magazine;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.util.Optional.ofNullable;

/**
 * @author Alexander Diachenko.
 */
public class Makeup extends AbstractMagazine {

    private static final String DATA_VARIANT_ID = "data-variant-id";
    private static final String DISCOUNT_PRICE = "span.product-item__price > span.rus";
    private static final String NORMAL_PRICE = "data-price";
    private static final String SITE_DOMAIN = "makeup.com.ua";
    private static final String PRODUCT_STATUS = "product_enabled";
    private static final String IN_STOCK = "Есть в наличии";

    protected String getPriceFrom(Document document) {
        return ofNullable(document.select(DISCOUNT_PRICE).first())
                .map(Element::text)
                .or(() -> ofNullable(document.getElementsByAttributeValue(DATA_VARIANT_ID, getDataVariantId(url)).first())
                .map(element -> element.attr(NORMAL_PRICE)))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    protected String getSiteDomain() {
        return SITE_DOMAIN;
    }

    @Override
    public boolean isAvailable(Document document) {
        return ofNullable(document.getElementById(PRODUCT_STATUS))
                .map(Element::text)
                .filter(text -> StringUtils.containsIgnoreCase(text, IN_STOCK))
                .isPresent();
    }

    private String getDataVariantId(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }
}
