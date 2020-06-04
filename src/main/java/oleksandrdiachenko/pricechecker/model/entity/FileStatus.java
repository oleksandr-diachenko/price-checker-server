package oleksandrdiachenko.pricechecker.model.entity;


import lombok.*;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;
}
