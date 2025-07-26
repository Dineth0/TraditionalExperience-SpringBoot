package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraditionalItemRepo extends JpaRepository<Item, Integer> {
}
