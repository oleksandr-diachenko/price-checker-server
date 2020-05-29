package oleksandrdiachenko.pricechecker.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import oleksandrdiachenko.pricechecker.annotation.PhoneNumber;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "USR")
@Data
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "Nickname is mandatory")
    private String nickname;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @Email(message = "Email should be valid")
    private String email;
    @PhoneNumber(message = "Phone number should be in format +222 (22) 222-22-22 or +22 (222) 222-22-22")
    private String phone;
}
