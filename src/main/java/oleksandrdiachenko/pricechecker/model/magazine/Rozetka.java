package oleksandrdiachenko.pricechecker.model.magazine;

import oleksandrdiachenko.pricechecker.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static java.util.Optional.ofNullable;

public class Rozetka extends AbstractMagazine {

    private static final String DISCOUNT_PRICE = "detail-price-uah";
    private static final String SITE_DOMAIN = "rozetka.com.ua";
    private static final String BTN_LINK_I = "btn-link-i";
    private static final String BUY_TEXT = "Купить";

    @Override
    protected String getPriceFrom(Document document) {
        return ofNullable(document.getElementsByClass(DISCOUNT_PRICE).first())
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
        return document.getElementsByClass(BTN_LINK_I).stream()
                .map(Element::text)
                .anyMatch(BUY_TEXT::equalsIgnoreCase);
    }
}
