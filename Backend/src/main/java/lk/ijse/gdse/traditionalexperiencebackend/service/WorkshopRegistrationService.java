package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.TraditionalItemDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopRegistrationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WorkshopRegistrationService {
    WorkshopRegistrationDTO registerWorkshop(WorkshopRegistrationDTO workshopRegistrationDTO);
    List<Map<String, Object>> checkAvailability(Long workshopId, Date date);

    boolean cancelBooking(Long id);
    List<WorkshopRegistrationDTO> getBookingsByUserId(Long userId);
    List<WorkshopRegistrationDTO> getAlRegistrations();

    List<WorkshopRegistrationDTO> searchWorkshopRegistrations(Date date);

    public List<Map<String, Object>> getWorkshopBookingCounts();

    public Page<WorkshopRegistrationDTO> getWorkshopRegistrationsForPAge(Pageable pageable);


    int getTotalPages(int size);
}
