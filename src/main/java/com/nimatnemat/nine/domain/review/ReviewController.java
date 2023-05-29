package com.nimatnemat.nine.domain.review;

import com.nimatnemat.nine.domain.restaurant.RestaurantService;
import com.nimatnemat.nine.domain.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    @Autowired
    public UserService userService;
    @Autowired
    public RestaurantService restaurantService;

    @Autowired
    public ReviewController(ReviewRepository reviewRepository, ReviewService reviewService) {
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드 API", description = "리뷰에 사용될 이미지를 업로드합니다.")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "image") MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return new ResponseEntity<>("이미지 파일이 필요합니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            String imageUrl = reviewService.saveImageToGridFS(image);
            return new ResponseEntity<>(Collections.singletonMap("imageUrl", imageUrl), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("이미지 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createReview")
    @Operation(summary = "리뷰 작성 API", description = "리뷰를 POST합니다.")
    public ResponseEntity<?> createReview(@RequestParam("userId") String userId,
                                          @RequestParam Long restaurantId,
                                          @RequestBody ReviewDetail reviewDetail) {
        try {
            Review review = reviewService.createReview(userId, restaurantId, reviewDetail);
            restaurantService.updateAveragePreference(restaurantId);
            return new ResponseEntity<>(userId + "님의 리뷰가 작성되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/deleteReview/{reviewId}")
    @Operation(summary = "리뷰 삭제 API", description = "리뷰를 삭제합니다.")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return new ResponseEntity<>("리뷰가 삭제되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateReview/{reviewId}")
    @Operation(summary = "리뷰 업데이트 API", description = "리뷰를 업데이트합니다.")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId,
                                          @RequestBody ReviewDetail reviewDetail) {
        try {
            Optional<Review> optionalReview = reviewRepository.findByReviewId(reviewId);
            if (optionalReview.isPresent()) {
                Review review = optionalReview.get();
                reviewService.updateReview(reviewId, reviewDetail);
                restaurantService.updateAveragePreference(review.getRestaurantId());
                return new ResponseEntity<>("리뷰가 업데이트되었습니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 리뷰 조회 API", description = "특정 사용자가 작성한 모든 리뷰를 반환합니다.")
    public ResponseEntity<?> getAllReviewsByUserId(@PathVariable("userId") String userId) {
        try {
            List<Review> userReviews = reviewRepository.findByUserId(userId);
            List<ReviewDto> reviewDtoList = new ArrayList<>();

            for (Review review : userReviews) {
                String userNickName = userService.getUserNickName(review.getUserId());
                String restaurantName = restaurantService.getRestaurantName(review.getRestaurantId());

                review.setUserNickName(userNickName);
                review.setRestaurantName(restaurantName);

                ReviewDto reviewDto = new ReviewDto(review);
                reviewDtoList.add(reviewDto);
            }
            return new ResponseEntity<>(reviewDtoList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}