package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, Long> {
    boolean existsByInstructorName(String instructorName);

    Instructor findByInstructorName(String instructorName);
}
