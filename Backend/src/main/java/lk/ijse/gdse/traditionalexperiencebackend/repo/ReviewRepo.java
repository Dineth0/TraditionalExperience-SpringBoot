package lk.ijse.gdse.traditionalexperiencebackend.repo;

import lk.ijse.gdse.traditionalexperiencebackend.dto.ReviewDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Review;
import lk.ijse.gdse.traditionalexperiencebackend.entity.TraditionalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<ReviewDTO> findByWorkshopId(Long workshopId);

    List<Review> findByUserId(Long userId);

    Review findByTitle(String title);

    @Query(value = "SELECT r.* FROM review r " +
            "LEFT JOIN workshop w ON r.workshop_id = w.id " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Review> findReviewPaginated(@Param("limit") int limit, @Param("offset") int offset);

    @Query(value = "SELECT COUNT(*) FROM Review ", nativeQuery = true)
    int getTotalReviewCount();
}
