package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstructorDTO {
    private Long id;
    private String instructorName;
    private int age;
    private String category;
    private String instructorEmail;
    private String instructorPhone;
    private String image;
}
