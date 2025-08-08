package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lk.ijse.gdse.traditionalexperiencebackend.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String password;
    private RoleType role;
}
