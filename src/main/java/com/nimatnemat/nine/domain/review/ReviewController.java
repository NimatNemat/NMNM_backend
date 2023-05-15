package com.nimatnemat.nine.domain.review;

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

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

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
                                          @RequestBody ReviewDetail reviewDetail,
                                          @RequestParam(value = "imageUrls", required = false) List<String> imageUrls) {
        try {
            if (imageUrls == null) {
                imageUrls = new ArrayList<>();
            }
            Review review = reviewService.createReview(userId, restaurantId, reviewDetail, imageUrls);
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
                                          @RequestBody ReviewDetail reviewDetail,
                                          @RequestParam(value = "imageUrls", required = false) List<String> imageUrls) {
        try {
            if (imageUrls == null) {
                imageUrls = new ArrayList<>();
            }
            reviewService.updateReview(reviewId, reviewDetail, imageUrls);
            return new ResponseEntity<>("리뷰가 업데이트되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}