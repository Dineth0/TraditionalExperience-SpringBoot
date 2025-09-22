package lk.ijse.gdse.traditionalexperiencebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TraditionalItemDTO {
    private Long id;
    private String itemName;
    private String itemShortDescription;
    private String itemDescription;
    private List<String> itemImage;
}
