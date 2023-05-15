package com.nimatnemat.nine.domain.review;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewRepository reviewRepository, ReviewService reviewService) {
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
    }

    @PostMapping("/createReview")
    @Operation(summary = "리뷰 작성 API", description = "리뷰를 POST합니다.")
    public ResponseEntity<?> createReview(@RequestParam("userId") String userId, @RequestParam Long restaurantId, @RequestBody ReviewDetail reviewDetail) {
        try {
            Review review = reviewService.createReview(userId, restaurantId, reviewDetail);
            return new ResponseEntity<>(userId + "님의 리뷰가 작성되었습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}