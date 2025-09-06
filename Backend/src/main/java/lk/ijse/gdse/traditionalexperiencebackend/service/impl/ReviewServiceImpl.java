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

}
