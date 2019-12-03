package oleksandrdiachenko.pricechecker.model.magazine;

import oleksandrdiachenko.pricechecker.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * @author Alexander Diachenko
 */
public class Cosmetea extends AbstractMagazine {

    private static final String DISCOUNT_PRICE = "autocalc-product-special";
    private static final String NORMAL_PRICE = "autocalc-product-price";
    private static final String SITE_DOMAIN = "cosmetea.com.ua";
    private static final String OUT_OF_STOCK = "ul.description:contains(Доступность: Нет в наличии)";

    @Override
    protected String getPriceFrom(Document document) {
        return getElementByClass(document, DISCOUNT_PRICE)
                .or(() -> getElementByClass(document, NORMAL_PRICE))
                .map(Element::text)
                .map(StringUtil::formatPrice)
                .orElseThrow(IllegalStateException::new);
    }

    private Optional<Element> getElementByClass(Document document, String className) {
        return ofNullable(document.getElementsByClass(className).first());
    }

    @Override
    protected String getSiteDomain() {
        return SITE_DOMAIN;
    }

    @Override
    public boolean isAvailable(Document document) {
        Elements availabilities = document.select(OUT_OF_STOCK);
        return availabilities.isEmpty();
    }
}
