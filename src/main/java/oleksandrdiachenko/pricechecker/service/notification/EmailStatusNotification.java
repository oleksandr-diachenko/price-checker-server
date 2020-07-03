package oleksandrdiachenko.pricechecker.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.*;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import oleksandrdiachenko.pricechecker.service.dataservice.SettingService;
import oleksandrdiachenko.pricechecker.util.Reader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class EmailStatusNotification implements Notification<Status> {

    private final SettingService settingService;
    private final JavaMailSender javaMailSender;
    private final FileStatusService fileStatusService;
    private final EmailStatusNotificationHelper notificationHelper;
    private final Reader reader;

    @SneakyThrows
    @Override
    public void notify(User user, Status status, File... attachments) {
        Setting setting = settingService.findByUser(user);
        if (status == notificationHelper.getStatus() && setting.isNotifyByEmail()) {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject(notificationHelper.getEmailSubject());
            helper.setText(getHtmlEmailBody(), true);

            for (File file : attachments) {
                addAttachment(helper, file, getFileName(file));
            }

            javaMailSender.send(message);
        }
    }

    private String getHtmlEmailBody() {
        try {
            return reader.read(notificationHelper.getHtmlEmailBodyTemplate());
        } catch (IOException e) {
            log.error("Can't read email template. Returning default email");
            return "<h2>Hello.</h2><br>This email was auto generated.";
        }
    }

    private String getFileName(File file) {
        return fileStatusService.findByFileId(file.getId())
                .map(FileStatus::getName)
                .orElseGet(() -> file.getId() + ".xlsx");
    }

    @SneakyThrows
    private void addAttachment(MimeMessageHelper helper, File file, String fileName) {
        helper.addAttachment(fileName, new ByteArrayResource(file.getFile()));
    }
}
