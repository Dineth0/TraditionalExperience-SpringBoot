package lk.ijse.gdse.traditionalexperiencebackend.service.impl;

import lk.ijse.gdse.traditionalexperiencebackend.dto.ReviewDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopDTO;
import lk.ijse.gdse.traditionalexperiencebackend.dto.WorkshopRegistrationDTO;
import lk.ijse.gdse.traditionalexperiencebackend.entity.*;
import lk.ijse.gdse.traditionalexperiencebackend.repo.ReviewRepo;
import lk.ijse.gdse.traditionalexperiencebackend.repo.TraditionalItemRepo;
import lk.ijse.gdse.traditionalexperiencebackend.service.ReviewService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public int addReview(ReviewDTO reviewDTO) {
        try{
            Review review = modelMapper.map(reviewDTO, Review.class);

            if(reviewDTO.getWorkshopId() != null ){
                Workshop workshop = new Workshop();
                workshop.setId(reviewDTO.getWorkshopId());
                review.setWorkshop(workshop);
            }
            if(reviewDTO.getUserId() != null){
                User user = new User();
                user.setId(reviewDTO.getUserId());
                review.setUser(user);
            }
            reviewRepo.save(review);
            return VarList.Created;
        }catch(Exception e){
            return VarList.Bad_Gateway;
        }
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepo.findAll();
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();
    }

    @Override
    public List<ReviewDTO> getReviewsByWorkshop(Long workshopId) {
        if(workshopId == null){
            return new ArrayList<>();
        }
        List<ReviewDTO> reviews = reviewRepo.findByWorkshopId(workshopId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();
    }

    @Override
    public List<ReviewDTO> getReviewsByUser(Long userId) {
        if(userId == null){
            return new ArrayList<>();
        }
        List<Review> reviews = reviewRepo.findByUserId(userId);
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .toList();

    }

    @Override
    public int updateReview(ReviewDTO reviewDTO) {
        try{
            Review existingReview = reviewRepo.findById(reviewDTO.getId()).orElse(null);

            if(existingReview == null){
                return VarList.Not_Found;
            }
            Review newReview = reviewRepo.findByTitle(reviewDTO.getTitle());
            if(newReview != null && !newReview.getId().equals(reviewDTO.getId())){
                return VarList.Not_Acceptable;
            }
            existingReview.setVisitorName(reviewDTO.getVisitorName());
            existingReview.setRating(reviewDTO.getRating());
            existingReview.setTitle(reviewDTO.getTitle());
            existingReview.setDescription(reviewDTO.getDescription());
            existingReview.setWentDate(reviewDTO.getWentDate());

            reviewRepo.save(existingReview);
            return VarList.Updated;
        }catch(Exception e){
            return VarList.Bad_Gateway;
        }
    }

    @Override
    public boolean deleteReview(Long id) {
        if(reviewRepo.existsById(id)){
            reviewRepo.deleteById(id);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        return reviewRepo.findById(id)
                .map(review -> modelMapper.map(review, ReviewDTO.class))
                .orElse(null);
    }

}
