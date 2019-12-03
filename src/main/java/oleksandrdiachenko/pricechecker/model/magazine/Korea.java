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
public class Korea extends AbstractMagazine {

    private static final String DISCOUNT_PRICE = "div.summary > p > span > span";
    private static final String NORMAL_PRICE = "div.summary > p > span > ins > span";
    private static final String SITE_DOMAIN = "korea.in.ua";
    private static final String OUT_OF_STOCK = "out-of-stock";

    @Override
    protected String getPriceFrom(Document document) {
        return getElementByClass(document, DISCOUNT_PRICE)
                .or(() -> getElementByClass(document, NORMAL_PRICE))
                .map(Element::text)
                .map(StringUtil::formatPrice)
                .orElseThrow(IllegalStateException::new);
    }

    private Optional<Element> getElementByClass(Document document, String className) {
        return ofNullable(document.select(className).first());
    }

    @Override
    protected String getSiteDomain() {
        return SITE_DOMAIN;
    }

    @Override
    public boolean isAvailable(Document document) {
        Elements availabilities = document.getElementsByClass(OUT_OF_STOCK);
        return availabilities.isEmpty();
    }
}
