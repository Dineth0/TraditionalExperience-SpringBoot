package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;

import java.util.List;

public interface WorkshopService {
    int addWorkshop(WorkshopDTO workshopDTO);
    List<WorkshopDTO> getAllWorkshops();
    List<WorkshopDTO> getWorkshopsByItem(Long itemId);
    WorkshopDTO getWorkshopById(Long id);
    int getParticipantsById(Long id);
    int updateWorkshop(WorkshopDTO workshopDTO);
    boolean deleteWorkshop(Long id);
    public List<WorkshopDTO> getWorkshopsForPage(int page, int size);
    public int getTotalPages(int size);
    public List<WorkshopDTO> searchWorkshops(String keyword);
    int getTotalWorkshopCount();



}
