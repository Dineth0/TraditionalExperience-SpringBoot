package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lk.ijse.gdse.traditionalexperiencebackend.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private Long id;
    private double amount;
    private PaymentMethod paymentMethod;
    private String Status;
    private Date paymentDate;
    private Long userId;
    private Long registrationId;
}
