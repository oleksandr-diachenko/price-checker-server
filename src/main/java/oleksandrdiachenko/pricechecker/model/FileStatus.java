package oleksandrdiachenko.pricechecker.model;


import javax.persistence.*;

@Entity
@Table(name = "FILE_STATUS")
public class FileStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String status;
}
