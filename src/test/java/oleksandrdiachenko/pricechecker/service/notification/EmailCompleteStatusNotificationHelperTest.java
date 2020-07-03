package oleksandrdiachenko.pricechecker.service.notification;

import oleksandrdiachenko.pricechecker.model.entity.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static oleksandrdiachenko.pricechecker.model.entity.Status.COMPLETED;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/3/2020
 **/
@ExtendWith(MockitoExtension.class)
class EmailCompleteStatusNotificationHelperTest {

    @InjectMocks
    private EmailCompleteStatusNotificationHelper helper;

    @Test
    void shouldReturnCompleteStatus() {
        Status status = helper.getStatus();

        assertThat(status).isEqualTo(COMPLETED);
    }
}