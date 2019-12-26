package oleksandrdiachenko.pricechecker.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "FILE")
@Data
@EqualsAndHashCode
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private byte[] file;
    @OneToOne(fetch = FetchType.LAZY)
    private FileStatus fileStatus;
}
