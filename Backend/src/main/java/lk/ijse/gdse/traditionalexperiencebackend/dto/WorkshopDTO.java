package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkshopDTO {
    private Long id;
    private String title;
    private String description;
    private String duration;
    private String language;
    private int participantCount;
    private String fee;
    private String address;
    private String instructorName;
    private List<String> image;
    private List<String> time;
    private List<String> include;
    private Long itemId;
    private Long instructorId;
//    private String mapUrl;
}
