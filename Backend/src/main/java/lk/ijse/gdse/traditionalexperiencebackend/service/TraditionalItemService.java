package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.InstructorDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TraditionalItemService {
    int addItem(TraditionalItemDTO itemDTO);

    List<TraditionalItemDTO> getAllItems();

    TraditionalItemDTO getItemById(Long id);

    int updateItem(TraditionalItemDTO itemDTO);

    //    Page<TraditionalItemDTO> getAllItemsForPage(Pageable pageable);
    public List<TraditionalItemDTO> getItemsForPage(int page, int size);
    public int getTotalPages(int size);

    public List<TraditionalItemDTO> getItemsForCardPage(int page, int size);
    public int getTotalCardPages(int size);

    public List<TraditionalItemDTO> searchItems(String keyword);


    }
