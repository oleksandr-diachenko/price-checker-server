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
    @Column(length = 1000000)
    private byte[] file;
}
