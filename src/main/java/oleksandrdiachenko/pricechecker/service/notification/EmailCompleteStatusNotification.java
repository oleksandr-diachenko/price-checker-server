package oleksandrdiachenko.pricechecker.service.notification;

import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.SettingService;
import org.apache.commons.io.IOUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
public class EmailCompleteStatusNotification extends AbstractEmailStatusNotification {

    public EmailCompleteStatusNotification(SettingService settingService, JavaMailSender javaMailSender,
                                           FileStatusService fileStatusService) {
        super(settingService, javaMailSender, fileStatusService);
    }

    @Override
    protected Status getStatus() {
        return Status.COMPLETED;
    }

    @Override
    protected String getEmailSubject() {
        return "Price check complete";
    }

    @Override
    protected String getHtmlEmailBody() {
        try {
            return IOUtils.toString(getClass().getResourceAsStream("/templates/email/complete-email.html"), UTF_8);
        } catch (IOException e) {
            log.error("Can't read email template. Returning default email");
            return defaultEmail;
        }
    }
}
