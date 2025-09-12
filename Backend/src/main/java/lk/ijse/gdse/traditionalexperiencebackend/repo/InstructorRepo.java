package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, Long> {
    boolean existsByInstructorName(String instructorName);

    Instructor findByInstructorName(String instructorName);

    List<Instructor> findJobByInstructorNameContainingIgnoreCase(String keyword);

    @Query(value = "SELECT * FROM Instructor  LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Instructor> findInstructorPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM Instructor ", nativeQuery = true)
    int getTotalInstructorCount();
}
