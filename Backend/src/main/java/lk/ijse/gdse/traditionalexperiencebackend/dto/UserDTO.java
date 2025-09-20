package lk.ijse.gdse.traditionalexperiencebackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lk.ijse.gdse.traditionalexperiencebackend.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long id;
    @NotBlank(message = "username is null")
    private String username;
    @NotBlank(message = "email is null")
    @Email(message = "invalid Email")
    private String email;
    @NotBlank(message = "password is null")
    private String password;
    private RoleType role;
}
