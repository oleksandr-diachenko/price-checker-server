package oleksandrdiachenko.pricechecker.service.notification;

import oleksandrdiachenko.pricechecker.model.entity.Status;

import java.io.IOException;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/3/2020
 **/
public interface EmailStatusNotificationHelper {

    Status getStatus();

    String getEmailSubject();

    String getHtmlEmailBodyTemplate();
}
