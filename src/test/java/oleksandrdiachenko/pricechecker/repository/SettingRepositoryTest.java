package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.Setting;
import oleksandrdiachenko.pricechecker.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/6/2020
 **/
@DataJpaTest
class SettingRepositoryTest {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldReturnSettingByUser() {
        Setting setting = new Setting();
        User user = UserData.get();
        testEntityManager.persistAndFlush(user);
        setting.setUser(user);
        testEntityManager.persistAndFlush(setting);

        Setting settingByUser = settingRepository.findByUser(user);

        assertThat(settingByUser).isEqualTo(setting);
    }
}