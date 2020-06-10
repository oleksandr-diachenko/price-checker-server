package oleksandrdiachenko.pricechecker;

import lombok.RequiredArgsConstructor;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.SettingService;
import oleksandrdiachenko.pricechecker.service.notification.EmailCompleteStatusNotification;
import oleksandrdiachenko.pricechecker.service.notification.Notification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@Configuration
@RequiredArgsConstructor
public class NotificationConfiguration {

    private final SettingService settingService;
    private final JavaMailSender javaMailSender;
    private final FileStatusService fileStatusService;

    @Bean
    public Set<Notification<Status>> statusNotifications() {
        return Stream.of(new EmailCompleteStatusNotification(settingService, javaMailSender, fileStatusService))
                .collect(toSet());
    }
}
