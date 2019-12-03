package oleksandrdiachenko.pricechecker.model.magazine;

import oleksandrdiachenko.pricechecker.util.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static java.util.Optional.ofNullable;

/**
 * @author Alexander Diachenko
 */
public class SweetCorea extends AbstractMagazine {

    private static final String DISCOUNT_PRICE = "price";
    private static final String SITE_DOMAIN = "www.sweetcorea.com";
    private static final String OUT_OF_STOCK = "div.description:contains(Out Of Stock)";

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
        Elements availabilities = document.select(OUT_OF_STOCK);
        return availabilities.isEmpty();
    }
}
