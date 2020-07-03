package oleksandrdiachenko.pricechecker.service.dataservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oleksandrdiachenko.pricechecker.model.entity.ERole;
import oleksandrdiachenko.pricechecker.model.entity.Role;
import oleksandrdiachenko.pricechecker.repository.RoleRepository;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(ERole role) {
        Optional<Role> roleOptional = roleRepository.findByName(role);
        roleOptional.ifPresentOrElse(r -> log.info("Found role: {}", r),
                () -> log.info("Role with enum:{} not found", role));
        return roleOptional;
    }

    public Optional<Role> findById(long id) {
        Optional<Role> roleOptional = roleRepository.findById(id);
        roleOptional.ifPresentOrElse(role -> log.info("Found role: {}", role),
                () -> log.info("Role with id:{} not found", id));
        return roleOptional;
    }

    public List<Role> findAll() {
        List<Role> fileStatuses = IterableUtils.toList(roleRepository.findAll());
        log.info("Retrieved roles {}", fileStatuses);
        return fileStatuses;
    }
}
