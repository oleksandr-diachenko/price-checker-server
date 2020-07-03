package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static oleksandrdiachenko.pricechecker.model.entity.ERole.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author : Oleksandr Diachenko
 * @since : 7/6/2020
 **/
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldReturnRoleByNameWhenDatabaseReturnOne() {
        Role role = new Role();
        role.setName(ROLE_USER);
        entityManager.persistAndFlush(role);

        Optional<Role> optionalRole = roleRepository.findByName(ROLE_USER);

        assertThat(optionalRole).isPresent();
    }

    @Test
    void shouldReturnEmptyOptionalByNameWhenDatabaseDoesntReturnOne() {
        Role role = new Role();
        entityManager.persistAndFlush(role);

        Optional<Role> optionalRole = roleRepository.findByName(ROLE_USER);

        assertThat(optionalRole).isNotPresent();
    }
}