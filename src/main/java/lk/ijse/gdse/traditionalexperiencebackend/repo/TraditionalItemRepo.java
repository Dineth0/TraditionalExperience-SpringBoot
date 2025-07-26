package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraditionalItemRepo extends JpaRepository<TraditionalItem, Integer> {
}
