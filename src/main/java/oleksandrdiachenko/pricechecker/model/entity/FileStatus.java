package oleksandrdiachenko.pricechecker.model.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "FILE_STATUS")
@Data
@EqualsAndHashCode
public class FileStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String status;
    private long fileId;
}
