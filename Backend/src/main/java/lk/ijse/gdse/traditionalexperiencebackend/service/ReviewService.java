package lk.ijse.gdse.traditionalexperiencebackend.service;

import lk.ijse.gdse.traditionalexperiencebackend.dto.ReviewDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.Review;

import java.util.List;

public interface ReviewService {
    public int addReview(ReviewDTO reviewDTO);

    List<ReviewDTO> getAllReviews();
    List<ReviewDTO> getReviewsByWorkshop(Long workshopId);
    List<ReviewDTO> getReviewsByUser(Long userId);

    int updateReview(ReviewDTO reviewDTO);
    boolean deleteReview(Long id);
    ReviewDTO getReviewById(Long id);
}
