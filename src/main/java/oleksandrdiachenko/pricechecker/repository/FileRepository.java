package oleksandrdiachenko.pricechecker.repository;

import oleksandrdiachenko.pricechecker.model.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
}
