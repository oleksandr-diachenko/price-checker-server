package oleksandrdiachenko.pricechecker.model.magazine;

import oleksandrdiachenko.pricechecker.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.util.Optional.ofNullable;

/**
 * @author Alexander Diachenko
 */
public class RoseRoseShop extends AbstractMagazine {

    private static final String ITEMPROP = "itemprop";
    private static final String NORMAL_PRICE = "price";
    private static final String SITE_DOMAIN = "www.roseroseshop.com";
    private static final String BUTTON_CART = "button-cart";

    protected String getPriceFrom(Document document) {
        return ofNullable(document.getElementsByAttributeValue(ITEMPROP, NORMAL_PRICE).first())
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
        return ofNullable(document.getElementById(BUTTON_CART))
                .isPresent();
    }
}
