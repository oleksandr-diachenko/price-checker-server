package oleksandrdiachenko.pricechecker.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.Status;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EmailCompleteStatusNotificationHelper implements EmailStatusNotificationHelper {

    @Value("${email.complete.subject}")
    private String completeEmailSubject;
    @Value("${email.complete.template}")
    private String completeEmailTemplatePath;


    @Override
    public Status getStatus() {
        return Status.COMPLETED;
    }

    @Override
    public String getEmailSubject() {
        return completeEmailSubject;
    }

    @Override
    public String getHtmlEmailBodyTemplate() {
        return completeEmailTemplatePath;
    }
}
