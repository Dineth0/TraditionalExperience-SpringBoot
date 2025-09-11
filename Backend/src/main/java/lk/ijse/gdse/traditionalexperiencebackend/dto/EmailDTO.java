package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailDTO {
    private Long id;
    private String subject;
    private String body;
    private LocalDateTime createdAt;
    private Long instructorId;
}
