package oleksandrdiachenko.pricechecker.model.magazine;

import oleksandrdiachenko.pricechecker.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.util.Optional.ofNullable;

/**
 * @author Alexander Diachenko
 */
public class Sweetness extends AbstractMagazine {

    private static final String DISCOUNT = "our_price_display";
    private static final String SITE_DOMAIN = "sweetness.com.ua";
    private static final String ADD_TO_CART = "add_to_cart";

    @Override
    protected String getPriceFrom(Document document) {
        return ofNullable(document.getElementById(DISCOUNT))
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
        return ofNullable(document.getElementById(ADD_TO_CART)).isPresent();
    }
}
