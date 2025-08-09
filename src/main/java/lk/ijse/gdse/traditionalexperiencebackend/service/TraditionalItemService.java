package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;

import java.util.List;

public interface TraditionalItemService {
    void addItem(TraditionalItemDTO itemDTO);
    List<TraditionalItemDTO> getAllItems();
}
