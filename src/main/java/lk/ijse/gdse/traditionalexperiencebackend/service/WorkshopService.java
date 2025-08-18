package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;

import java.util.List;

public interface WorkshopService {
    int addWorkshop(WorkshopDTO workshopDTO);
    List<WorkshopDTO> getAllWorkshops();
}
