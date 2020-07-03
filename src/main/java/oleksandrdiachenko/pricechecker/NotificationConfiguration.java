package oleksandrdiachenko.pricechecker;

import lombok.RequiredArgsConstructor;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import oleksandrdiachenko.pricechecker.service.dataservice.SettingService;
import oleksandrdiachenko.pricechecker.service.notification.EmailCompleteStatusNotificationHelper;
import oleksandrdiachenko.pricechecker.service.notification.EmailStatusNotificationHelper;
import oleksandrdiachenko.pricechecker.service.notification.EmailStatusNotification;
import oleksandrdiachenko.pricechecker.service.notification.Notification;
import oleksandrdiachenko.pricechecker.util.Reader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class NotificationConfiguration {

    private final SettingService settingService;
    private final JavaMailSender javaMailSender;
    private final FileStatusService fileStatusService;
    private final EmailCompleteStatusNotificationHelper emailCompleteStatusNotification;
    private final Reader reader;

    @Bean
    public Set<Notification<Status>> statusNotifications() {
        return Set.of(getEmailStatusNotification(emailCompleteStatusNotification));
    }

    private EmailStatusNotification getEmailStatusNotification(EmailStatusNotificationHelper helper) {
        return new EmailStatusNotification(settingService, javaMailSender, fileStatusService, helper, reader);
    }
}
