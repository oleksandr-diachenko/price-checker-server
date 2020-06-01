package oleksandrdiachenko.pricechecker.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
@Data
@EqualsAndHashCode
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;
}