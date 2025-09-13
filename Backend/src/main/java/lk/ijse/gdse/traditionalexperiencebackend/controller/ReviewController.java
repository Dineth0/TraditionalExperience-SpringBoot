package lk.ijse.gdse.traditionalexperiencebackend.controller;

import lk.ijse.gdse.traditionalexperiencebackend.dto.*;
import lk.ijse.gdse.traditionalexperiencebackend.service.ReviewService;
import lk.ijse.gdse.traditionalexperiencebackend.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(value = "/addReview")
    public ResponseEntity<ResponseDTO> addReview(@RequestBody ReviewDTO reviewDTO) {

        try {

            int response = reviewService.addReview(reviewDTO);
            if (response == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Workshop Saved", reviewDTO));
            }else {
                System.out.println(response);
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error While Saving", null));

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"file Upload Failed" + e.getMessage(),null));


        }
    }

    @GetMapping("/getAllReviews")
    public ResponseEntity<ResponseDTO> getAllReviews() {
        try{
            List<ReviewDTO> reviews = reviewService.getAllReviews();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK,"suceess", reviews));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getReviewByWorkshop/{workshopId}")
    public ResponseEntity<ResponseDTO> getReviewByWorkshop(@PathVariable Long workshopId){
        try{
            List<ReviewDTO> reviews = reviewService.getReviewsByWorkshop(workshopId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.OK, "Success", reviews));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getReviewByUser/{userId}")
    public ResponseEntity<ResponseDTO> getReviewByUser(@PathVariable Long userId){
        List<ReviewDTO> reviews = reviewService.getReviewsByUser(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDTO(VarList.OK, "Success", reviews));
    }

    @PutMapping("/updateReview")
    public ResponseEntity<ResponseDTO> updateReview(@RequestBody ReviewDTO reviewDTO) {
        try {
            int response = reviewService.updateReview(reviewDTO);
            switch (response) {
                case VarList.Updated -> {
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Updated, "Review Saved", reviewDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Review Already exists", null));

                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error While Saving", null));

                }
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,"file Upload Failed" + e.getMessage(),null));


        }
    }

    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<ResponseDTO> deleteReview(@PathVariable Long id) {
        try{
            boolean deleted = reviewService.deleteReview(id);
            if(deleted){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Review Deleted", null));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Review Not Found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getReviewById/{id}")
    public ResponseEntity<ResponseDTO> getReviewById(@PathVariable Long id) {
        try{
            ReviewDTO reviewDTO = reviewService.getReviewById(id);
            if(reviewDTO != null){
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.OK, "Success", reviewDTO));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Workshop Not Found", null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @GetMapping("/paginated")
    public List<ReviewDTO> getPaginatedItems(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "2") int size) {
        return reviewService.getReviewsForPage(page, size);
    }

    @GetMapping("/total-pages")
    public int getTotalPages(@RequestParam(defaultValue = "2") int size) {
        return reviewService.getTotalPages(size);
    }

}
