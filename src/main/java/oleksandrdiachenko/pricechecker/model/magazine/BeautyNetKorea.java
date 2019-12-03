package oleksandrdiachenko.pricechecker.model.magazine;

import oleksandrdiachenko.pricechecker.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * @author Alexander Diachenko
 */
public class BeautyNetKorea extends AbstractMagazine {

    private static final String DISCOUNT_PRICE = "span_product_price_sale";
    private static final String NORMAL_PRICE = "span_product_price_text";
    private static final String SITE_DOMAIN = "beautynetkorea.com";
    private static final String OUT_OF_STOCK = "Out-of-stock";
    private static final String ALT = "alt";

    @Override
    protected String getPriceFrom(Document document) {
        return getElementById(document, DISCOUNT_PRICE)
                .or(() -> getElementById(document, NORMAL_PRICE))
                .map(Element::text)
                .map(this::formatPrice)
                .orElseThrow(IllegalStateException::new);
    }

    private Optional<Element> getElementById(Document document, String id) {
        return ofNullable(document.getElementById(id));
    }

    private String formatPrice(String price) {
        String priceUSD = StringUtils.substringBetween(price, "(", ")");
        return StringUtil.formatPrice(priceUSD);
    }

    @Override
    protected String getSiteDomain() {
        return SITE_DOMAIN;
    }

    @Override
    public boolean isAvailable(Document document) {
        Elements availabilities = document.getElementsByAttributeValue(ALT, OUT_OF_STOCK);
        return availabilities.isEmpty();
    }
}
