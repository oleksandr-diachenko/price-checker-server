package oleksandrdiachenko.pricechecker.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import oleksandrdiachenko.pricechecker.annotation.PhoneNumber;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USR")
@Data
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 20)
    private String username;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 5, max = 120)
    private String password;
    @Email(message = "Email should be valid")
    @Size(max = 50)
    private String email;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
