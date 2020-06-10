package oleksandrdiachenko.pricechecker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import oleksandrdiachenko.pricechecker.model.entity.Setting;
import oleksandrdiachenko.pricechecker.model.entity.User;
import oleksandrdiachenko.pricechecker.repository.SettingRepository;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public Setting save(Setting setting) {
        Setting savedSetting = settingRepository.save(setting);
        log.info("Setting {} saved", savedSetting);
        return savedSetting;
    }

    public Setting findByUser(User user) {
        Setting setting = settingRepository.findByUser(user);
        log.info("Retrieved for user with id {} setting {}", user.getId(), setting);
        return setting;
    }
}
