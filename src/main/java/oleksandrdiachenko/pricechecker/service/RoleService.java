package oleksandrdiachenko.pricechecker.service;

import lombok.RequiredArgsConstructor;
import oleksandrdiachenko.pricechecker.model.entity.ERole;
import oleksandrdiachenko.pricechecker.model.entity.Role;
import oleksandrdiachenko.pricechecker.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(ERole role) {
        return roleRepository.findByName(role);
    }
}
