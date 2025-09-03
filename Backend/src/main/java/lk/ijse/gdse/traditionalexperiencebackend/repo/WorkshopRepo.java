package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkshopRepo extends JpaRepository<Workshop, Long> {
    List<Workshop> findByItemId(Long itemId);
    @Query("SELECT w.participantCount FROM Workshop w WHERE w.id = :id")
    Integer getParticipantCountById(@Param("id") Long id);

    Workshop findByTitle(String title);
}
