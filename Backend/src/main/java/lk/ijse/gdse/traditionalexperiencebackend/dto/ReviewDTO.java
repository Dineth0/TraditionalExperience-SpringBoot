package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewDTO {
    private Long id;
    private String visitorName;
    private int rating;
    private String title;
    private String description;
    private Date wentDate;
    private Date reviewDate;
    private Long userId;
    private Long workshopId;
    private String workshopName;

}
