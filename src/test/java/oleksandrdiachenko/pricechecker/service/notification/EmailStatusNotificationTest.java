package oleksandrdiachenko.pricechecker.service.notification;

import oleksandrdiachenko.pricechecker.model.entity.File;
import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.Setting;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.service.dataservice.FileStatusService;
import oleksandrdiachenko.pricechecker.service.dataservice.SettingService;
import oleksandrdiachenko.pricechecker.util.Reader;
import org.apache.commons.mail.util.MimeMessageParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.activation.DataSource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;

import static oleksandrdiachenko.pricechecker.model.entity.Status.COMPLETED;
import static oleksandrdiachenko.pricechecker.model.entity.Status.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/3/2020
 **/
@ExtendWith(MockitoExtension.class)
class EmailStatusNotificationTest {

    public static final String XLSX = "1.xlsx";
    private static final String EMAIL = "mail@mail.com";
    private static final String EMAIL_SUBJECT = "Completed subject";
    private static final String TEMPLATE_PATH = "/path/email-template.html";
    private static final String HTML = "<body>Template</body>";
    private static final byte[] BYTES = {1, 2, 3};
    private static final Long FILE_ID = 1L;
    private static final String FILE_NAME = "filename.xls";
    private static final String FILENAME_WITH_ID = "1.xlsx";

    @InjectMocks
    private EmailStatusNotification notification;

    @Mock
    private SettingService settingService;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private FileStatusService fileStatusService;
    @Mock
    private EmailStatusNotificationHelper notificationHelper;
    @Mock
    private User user;
    @Mock
    private File file;
    @Mock
    private Setting setting;
    @Mock
    private Reader reader;
    @Mock
    private FileStatus fileStatus;
    private final MimeMessage mimeMessage = new JavaMailSenderImpl().createMimeMessage();

    @Test
    void shouldNotSendWhenNotifyByEmailIsDisabled() {
        when(settingService.findByUser(user)).thenReturn(setting);
        when(notificationHelper.getStatus()).thenReturn(COMPLETED);

        notification.notify(user, COMPLETED);

        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void shouldNotSetWhenStatusIsNotCompleted() {
        when(notificationHelper.getStatus()).thenReturn(COMPLETED);

        notification.notify(user, IN_PROGRESS);

        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void shouldSendEmailWithoutAttachmentsWhenFilesNotProvided() throws Exception {
        when(settingService.findByUser(user)).thenReturn(setting);
        when(notificationHelper.getStatus()).thenReturn(COMPLETED);
        when(setting.isNotifyByEmail()).thenReturn(true);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(user.getEmail()).thenReturn(EMAIL);
        when(notificationHelper.getEmailSubject()).thenReturn(EMAIL_SUBJECT);
        when(notificationHelper.getHtmlEmailBodyTemplate()).thenReturn(TEMPLATE_PATH);
        when(reader.read(TEMPLATE_PATH)).thenReturn(HTML);

        notification.notify(user, COMPLETED);

        MimeMessageParser messageParser = new MimeMessageParser(mimeMessage).parse();
        assertThat(messageParser.getSubject()).isEqualTo(EMAIL_SUBJECT);
        assertThat(messageParser.getHtmlContent()).isEqualTo(HTML);
        assertThat(messageParser.getTo()).isEqualTo(List.of(new InternetAddress(EMAIL)));
        assertThat(messageParser.getAttachmentList()).isEmpty();
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void shouldSendEmailWithAttachmentsWithFileNameWhenFilesProvidedAndFileStatusExist() throws Exception {
        when(settingService.findByUser(user)).thenReturn(setting);
        when(notificationHelper.getStatus()).thenReturn(COMPLETED);
        when(setting.isNotifyByEmail()).thenReturn(true);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(user.getEmail()).thenReturn(EMAIL);
        when(notificationHelper.getEmailSubject()).thenReturn(EMAIL_SUBJECT);
        when(notificationHelper.getHtmlEmailBodyTemplate()).thenReturn(TEMPLATE_PATH);
        when(reader.read(TEMPLATE_PATH)).thenReturn(HTML);
        when(file.getFile()).thenReturn(BYTES);
        when(file.getId()).thenReturn(FILE_ID);
        when(fileStatusService.findByFileId(FILE_ID)).thenReturn(Optional.of(fileStatus));
        when(fileStatus.getName()).thenReturn(FILE_NAME);

        notification.notify(user, COMPLETED, file);

        MimeMessageParser messageParser = new MimeMessageParser(mimeMessage).parse();
        assertThat(messageParser.getSubject()).isEqualTo(EMAIL_SUBJECT);
        assertThat(messageParser.getHtmlContent()).isEqualTo(HTML);
        assertThat(messageParser.getTo()).isEqualTo(List.of(new InternetAddress(EMAIL)));
        DataSource attachment = messageParser.findAttachmentByName(FILE_NAME);
        assertThat(attachment.getInputStream().readAllBytes()).isEqualTo(BYTES);
        assertThat(attachment.getName()).isEqualTo(FILE_NAME);
        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void shouldSendEmailWithAttachmentsWithFileNameAsFileIdWhenFilesProvidedAndFileStatusNotExist() throws Exception {
        when(settingService.findByUser(user)).thenReturn(setting);
        when(notificationHelper.getStatus()).thenReturn(COMPLETED);
        when(setting.isNotifyByEmail()).thenReturn(true);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(user.getEmail()).thenReturn(EMAIL);
        when(notificationHelper.getEmailSubject()).thenReturn(EMAIL_SUBJECT);
        when(notificationHelper.getHtmlEmailBodyTemplate()).thenReturn(TEMPLATE_PATH);
        when(reader.read(TEMPLATE_PATH)).thenReturn(HTML);
        when(file.getFile()).thenReturn(BYTES);
        when(file.getId()).thenReturn(FILE_ID);
        when(fileStatusService.findByFileId(FILE_ID)).thenReturn(Optional.empty());

        notification.notify(user, COMPLETED, file);

        MimeMessageParser messageParser = new MimeMessageParser(mimeMessage).parse();
        assertThat(messageParser.getSubject()).isEqualTo(EMAIL_SUBJECT);
        assertThat(messageParser.getHtmlContent()).isEqualTo(HTML);
        assertThat(messageParser.getTo()).isEqualTo(List.of(new InternetAddress(EMAIL)));
        DataSource attachment = messageParser.findAttachmentByName(FILENAME_WITH_ID);
        assertThat(attachment.getInputStream().readAllBytes()).isEqualTo(BYTES);
        assertThat(attachment.getName()).isEqualTo(FILENAME_WITH_ID);
        verify(javaMailSender).send(mimeMessage);
    }
}