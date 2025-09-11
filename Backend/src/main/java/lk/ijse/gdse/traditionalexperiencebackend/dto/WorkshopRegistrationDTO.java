package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkshopRegistrationDTO {
    private Long id;
    private String email;
    private String phone;
    private String country;
    private Date registrationDate;
    private Date selectWorkshopDate;
    private String workshopTime;
    private int member;
    private double totalFee;
    private String PaymentStatus;
    private boolean isEmailSent;
    private Long userId;
    private Long workshopId;
    private String workshopName;
    private String instructorName;
}
