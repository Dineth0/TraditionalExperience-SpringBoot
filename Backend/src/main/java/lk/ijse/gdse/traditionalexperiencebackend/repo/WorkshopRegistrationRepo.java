package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface WorkshopRegistrationRepo extends JpaRepository<WorkshopRegistration, Long> {
    boolean existsByWorkshopTime(String workshopTime);


    boolean existsByWorkshopTimeAndSelectWorkshopDateAndWorkshopId(String workshopTime, Date selectWorkshopDate, Long workshop_id);

    List<WorkshopRegistration> findByUserId(Long userId);

//    List<WorkshopRegistration> findWorkshopRegistrationBySelectWorkshopDateContainingIgnoreCase(int keyword);

//    List<WorkshopRegistration> findWorkshopRegistrationBySelectWorkshopDateContainingIgnoreCase(int keyword);
}
