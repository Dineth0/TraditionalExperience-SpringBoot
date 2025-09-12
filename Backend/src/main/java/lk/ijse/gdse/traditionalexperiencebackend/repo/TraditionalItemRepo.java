package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TraditionalItemRepo extends JpaRepository<TraditionalItem, Long> {
    boolean existsByItemName(String itemName);

    TraditionalItem findByItemName(String itemName);
//    void findById(UUID id);
}
