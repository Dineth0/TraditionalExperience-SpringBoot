package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkshopDTO {
    private String id;
    private String title;
    private String description;
    private String location;
    private String duration;
    private String language;
    private String fee;
    private String image;
//    private String mapUrl;
}
