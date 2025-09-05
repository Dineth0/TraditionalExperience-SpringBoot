package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopRegistrationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WorkshopRegistrationService {
    int registerWorkshop(WorkshopRegistrationDTO workshopRegistrationDTO);
    List<Map<String, Object>> checkAvailability(Long workshopId, Date date);

    boolean cancelBooking(Long id);
    List<WorkshopRegistrationDTO> getBookingsByUserId(Long userId);

}
