package oleksandrdiachenko.pricechecker.service.notification;

import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.User;

public interface Notification<T> {

    void notify(User user, T t, File... attachments);
}
