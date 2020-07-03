package oleksandrdiachenko.pricechecker.service.dataservice;

import oleksandrdiachenko.pricechecker.model.entity.Setting;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.SettingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/6/2020
 **/
@ExtendWith(MockitoExtension.class)
class SettingServiceTest {

    @InjectMocks
    private SettingService settingService;

    @Mock
    private SettingRepository settingRepository;

    @Test
    void shouldCallSaveWhenSaveMethodExecuted() {
        Setting setting = new Setting();

        settingService.save(setting);

        verify(settingRepository).save(setting);
    }

    @Test
    void shouldReturnSettingByUserWhenRepositoryReturnOne() {
        User user = new User();
        Setting setting = new Setting();
        when(settingRepository.findByUser(user)).thenReturn(setting);

        Setting settingByUser = settingService.findByUser(user);

        assertThat(settingByUser).isEqualTo(setting);
    }
}