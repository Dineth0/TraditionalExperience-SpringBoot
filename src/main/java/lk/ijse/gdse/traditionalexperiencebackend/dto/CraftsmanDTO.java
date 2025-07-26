package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CraftsmanDTO {
    private String id;
    private String craftsmanName;
    private int age;
    private String craftsmanEmail;
    private String craftsmanPhone;
    private String image;
}
