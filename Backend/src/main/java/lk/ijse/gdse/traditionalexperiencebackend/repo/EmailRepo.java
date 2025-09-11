package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepo extends JpaRepository<Email, Long> {
}
