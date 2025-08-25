package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface WorkshopRegistrationRepo extends JpaRepository<WorkshopRegistration, Long> {
    boolean existsByWorkshopTime(String workshopTime);


    boolean existsByWorkshopTimeAndSelectWorkshopDateAndWorkshopId(String workshopTime, Date selectWorkshopDate, Long workshop_id);
}
