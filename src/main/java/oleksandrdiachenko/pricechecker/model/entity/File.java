package oleksandrdiachenko.pricechecker.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "FILE")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private byte[] file;
    @OneToOne(fetch = FetchType.LAZY)
    private FileStatus fileStatus;
}
