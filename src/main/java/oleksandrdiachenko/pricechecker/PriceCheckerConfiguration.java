package oleksandrdiachenko.pricechecker;

import oleksandrdiachenko.pricechecker.model.magazine.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Configuration
public class PriceCheckerConfiguration {

    @Bean
    public Set<Magazine> magazines() {
        return Stream.of(new Makeup(), new Korea(), new RoseRoseShop(),
                new BeautyNetKorea(), new NowZenith(), new Rozetka(), new KoreaButik(),
                new SweetCorea(), new Cosmetea(), new Sweetness()).collect(toSet());
    }

    @Bean
    public Set<String> excelTypes() {
        return Stream.of("application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .collect(toSet());
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }
}
