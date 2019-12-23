package oleksandrdiachenko.pricechecker;

import oleksandrdiachenko.pricechecker.model.magazine.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class PriceCheckerConfiguration {

    @Bean
    public List<Magazine> getMagazines() {
        List<Magazine> magazines = new ArrayList<>();
        magazines.add(new Makeup());
        magazines.add(new Korea());
        magazines.add(new RoseRoseShop());
        magazines.add(new BeautyNetKorea());
        magazines.add(new NowZenith());
        magazines.add(new Rozetka());
        magazines.add(new KoreaButik());
        magazines.add(new SweetCorea());
        magazines.add(new Cosmetea());
        magazines.add(new Sweetness());
        return magazines;
    }

    @Bean
    public List<String> getExcelTypes() {
        return Arrays.asList("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}
