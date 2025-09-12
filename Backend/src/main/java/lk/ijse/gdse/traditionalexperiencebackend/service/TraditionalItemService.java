package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;

import java.util.List;
import java.util.UUID;

public interface TraditionalItemService {
    int addItem(TraditionalItemDTO itemDTO);
    List<TraditionalItemDTO> getAllItems();

    TraditionalItemDTO getItemById(Long id);
    int updateItem(TraditionalItemDTO itemDTO);
}
