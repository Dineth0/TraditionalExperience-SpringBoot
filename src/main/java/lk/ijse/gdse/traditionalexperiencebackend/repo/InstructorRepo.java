package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, Integer> {
    boolean existsByInstructorName(String instructorName);
}
