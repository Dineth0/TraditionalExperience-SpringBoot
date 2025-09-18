package lk.ijse.gdse.traditionalexperiencebackend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PaymentMethod paymentMethod;
    private String status;
    private Date paymentDate;
    private Long userId;
    private Long registrationId;
    private Long workshopId;
    private String workshopName;
    private String userName;
}

