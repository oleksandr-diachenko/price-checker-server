package oleksandrdiachenko.pricechecker;

import com.epam.pricecheckercore.service.checker.Checker;
import com.epam.pricecheckercore.service.checker.PriceChecker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Configuration
@EnableAsync
public class PriceCheckerConfiguration {

    @Bean
    public Checker checker() {
        return new PriceChecker();
    }

    @Bean
    public Set<String> excelTypes() {
        return Stream.of("application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .collect(toSet());
    }
}
