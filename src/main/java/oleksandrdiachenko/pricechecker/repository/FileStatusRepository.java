package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.entity.FileStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStatusRepository extends CrudRepository<FileStatus, Long> {
}
