package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean readStatus;
    private Date createAt;
    private Long userId;
    private Long workshopId;
}
