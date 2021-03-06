package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
