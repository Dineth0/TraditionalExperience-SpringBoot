package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.entity.Instructor;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TraditionalItemRepo extends JpaRepository<TraditionalItem, Long> {
    boolean existsByItemName(String itemName);

    TraditionalItem findByItemName(String itemName);


//    void findById(UUID id);

    @Query(value = "SELECT * FROM traditional_item  LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<TraditionalItem> findTraditionalItemPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM traditional_item ", nativeQuery = true)
    int getTotalTraditionalItemCount();

    List<TraditionalItem> findTraditionalItemByItemNameContainingIgnoreCase(String keyword);
}
