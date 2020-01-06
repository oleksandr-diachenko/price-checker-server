package oleksandrdiachenko.pricechecker.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FILE_STATUS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FileStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String status;
    private long fileId;
    private LocalDateTime acceptedTime;
}
