package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import oleksandrdiachenko.pricechecker.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStatusRepository extends CrudRepository<FileStatus, Long> {
    Iterable<FileStatus> findByUser(User user);
}
