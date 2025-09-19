package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import lk.ijse.gdse.traditionalexperiencebackend.entity.WorkshopRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WorkshopRegistrationRepo extends JpaRepository<WorkshopRegistration, Long> {
    boolean existsByWorkshopTime(String workshopTime);


    boolean existsByWorkshopTimeAndSelectWorkshopDateAndWorkshopId(String workshopTime, Date selectWorkshopDate, Long workshop_id);

    List<WorkshopRegistration> findByUserId(Long userId);

//    List<WorkshopRegistration> findWorkshopRegistrationBySelectWorkshopDateContainingIgnoreCase(int keyword);

//    List<WorkshopRegistration> findWorkshopRegistrationBySelectWorkshopDateContainingIgnoreCase(int keyword);

    @Query("SELECT w.title AS title, COUNT(r.id) AS totalBookings " +
            "FROM WorkshopRegistration r JOIN r.workshop w " +
            "GROUP BY w.title ORDER BY totalBookings DESC")
    List<Map<String,Object>> getWorkshopBookingCounts();

    @Query("SELECT r FROM WorkshopRegistration r LEFT JOIN FETCH r.workshop w LEFT JOIN FETCH w.instructor WHERE r.selectWorkshopDate = :date")
    List<WorkshopRegistration> findBySelectWorkshopDateWithDetails(@Param("date") Date date);


    @Query(value = "SELECT COUNT(*) FROM WorkshopRegistration ", nativeQuery = true)
    int getTotalWorkshopRegistrationCount();
}
