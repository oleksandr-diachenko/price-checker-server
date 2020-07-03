package oleksandrdiachenko.pricechecker;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:email-notification.properties")
public class PropertyConfiguration {
}
