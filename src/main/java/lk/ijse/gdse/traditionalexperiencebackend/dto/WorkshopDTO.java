package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private int participantCount;
    private String fee;
    private List<String> image;
    private String itemId;
    private String instructorId;
//    private String mapUrl;
}
