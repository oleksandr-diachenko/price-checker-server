package oleksandrdiachenko.pricechecker.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.*;
import oleksandrdiachenko.pricechecker.service.FileStatusService;
import oleksandrdiachenko.pricechecker.service.SettingService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractEmailStatusNotification implements Notification<Status> {

    private final SettingService settingService;
    private final JavaMailSender javaMailSender;
    private final FileStatusService fileStatusService;
    protected final String defaultEmail = "<h2>Hello.</h2><br>This email was auto generated.";

    @SneakyThrows
    @Override
    public void notify(User user, Status status, File... attachments) {
        Setting setting = settingService.findByUser(user);
        if (status == getStatus() && setting.isNotifyByEmail()) {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject(getEmailSubject());
            helper.setText(getHtmlEmailBody(), true);

            for (File file : attachments) {
                addAttachment(helper, file, getFileName(file));
            }

            javaMailSender.send(message);
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

    protected abstract Status getStatus();

    protected abstract String getEmailSubject();

    protected abstract String getHtmlEmailBody();
}
