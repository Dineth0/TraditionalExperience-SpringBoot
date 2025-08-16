package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstructorDTO {
    private String id;
    private String instructorName;
    private int age;
    private String category;
    private String instructorEmail;
    private String instructorPhone;
    private String image;
}
