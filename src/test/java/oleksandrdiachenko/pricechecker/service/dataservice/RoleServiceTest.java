package oleksandrdiachenko.pricechecker.service.dataservice;

import oleksandrdiachenko.pricechecker.model.entity.Role;
import oleksandrdiachenko.pricechecker.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static oleksandrdiachenko.pricechecker.model.entity.ERole.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/6/2020
 **/
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void shouldReturnRoleByNameWhenRepositoryReturnOne() {
        Role role = new Role();
        when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.of(role));

        Optional<Role> optionalRole = roleService.findByName(ROLE_USER);

        assertThat(optionalRole).isPresent();
    }

    @Test
    void shouldReturnEmptyOptionalByNameWhenRepositoryDoesntReturnOne() {
        when(roleRepository.findByName(ROLE_USER)).thenReturn(Optional.empty());

        Optional<Role> optionalRole = roleService.findByName(ROLE_USER);

        assertThat(optionalRole).isNotPresent();
    }

    @Test
    void shouldReturnRoleByIdWhenRepositoryReturnOne() {
        Role role = new Role();
        long id = 1L;
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        Optional<Role> optionalRole = roleService.findById(id);

        assertThat(optionalRole).isPresent();
    }

    @Test
    void shouldReturnEmptyOptionalByIdWhenRepositoryDoesntReturnOne() {
        long id = 1L;
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Role> optionalRole = roleService.findById(id);

        assertThat(optionalRole).isNotPresent();
    }

    @Test
    void shouldReturnListOfRolesWhenRepositoryReturnOne() {
        Role role = new Role();
        when(roleRepository.findAll()).thenReturn(List.of(role));

        List<Role> roles = roleService.findAll();

        assertThat(roles).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListOfRolesWhenRepositoryReturnOne() {
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        List<Role> roles = roleService.findAll();

        assertThat(roles).isEmpty();
    }
}