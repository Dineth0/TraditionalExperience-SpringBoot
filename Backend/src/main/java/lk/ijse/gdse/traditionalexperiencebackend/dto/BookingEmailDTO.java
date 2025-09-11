package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingEmailDTO {
    private Long registrationId;
    private String instructorEmail;
}
