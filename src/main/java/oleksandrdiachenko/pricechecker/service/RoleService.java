package oleksandrdiachenko.pricechecker.service;

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
        return roleRepository.findByName(role);
    }

    public Optional<Role> findById(long id) {
        return roleRepository.findById(id);
    }

    public List<Role> findAll() {
        List<Role> fileStatuses = IterableUtils.toList(roleRepository.findAll());
        log.info("Retrieved roles {}", fileStatuses);
        return fileStatuses;
    }
}
