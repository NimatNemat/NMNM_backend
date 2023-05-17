package com.nimatnemat.nine.domain.review;

import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private  ReviewRepository reviewRepository;
    @Autowired
    private GridFSBucket gridFsBucket;
    @Autowired
    private ReviewCounterRepository reviewCounterRepository;

//    @Autowired
//    public ReviewService(ReviewRepository reviewRepository) {
//        this.reviewRepository = reviewRepository;
//    }
    // 이미지를 GridFS에 저장하고 저장된 이미지의 ObjectId를 반환하는 메소드
    public String saveImageToGridFS(MultipartFile image) {
        try {
            // 이미지의 입력 스트림을 얻습니다.
            InputStream inputStream = image.getInputStream();
            // 이미지의 원본 파일명을 얻습니다.
            String filename = image.getOriginalFilename();

            // 이미지를 GridFS에 저장합니다.
            ObjectId objectId = gridFsBucket.uploadFromStream(filename, inputStream);

            // 저장된 이미지의 URL을 생성하고 반환합니다.
            String imageUrl = String.format("http://3.39.232.5:8080/images/%s", objectId.toString());
            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("이미지를 GridFS에 저장하는 동안 오류가 발생했습니다.", e);
        }
    }
    public Review createReview(String userId, Long restaurantId, ReviewDetail reviewDetail) {
        // ReviewCounter를 조회하고 업데이트
        ReviewCounter reviewCounter = reviewCounterRepository.findById("unique").orElseGet(ReviewCounter::new);
        Long reviewId = reviewCounter.incrementAndGet();
        reviewCounterRepository.save(reviewCounter);

        // 새 Review 객체를 생성하고 필드를 설정
        Review review = new Review();
        review.setReviewId(reviewId);

        // 새 Review 객체를 생성하고 필드를 설정
        review.setUserId(userId);
        review.setRestaurantId(restaurantId); // restaurantId가 int형이므로 변환 필요
        review.setReviewImage(reviewDetail.getReviewImage()); // ReviewDetail에서 이미지 URL 리스트를 가져와서 Review에 설정
        review.setReviewInfo(reviewDetail.getReviewInfo());
        review.setReviewScore(reviewDetail.getReviewScore());
        review.setSimpleEvaluation(reviewDetail.getSimpleEvaluation());
        review.setReviewDate(new Date());

        // Review 객체를 데이터베이스에 저장하고 반환
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteByReviewId(reviewId);
    }

    public void updateReview(Long reviewId, ReviewDetail reviewDetail) {
        Review review = reviewRepository.findByReviewId(reviewId);
        if (review == null) {
            throw new RuntimeException("해당하는 리뷰가 없습니다.");
        }
        review.setReviewInfo(reviewDetail.getReviewInfo());
        review.setReviewScore(reviewDetail.getReviewScore());
        review.setReviewImage(reviewDetail.getReviewImage()); // ReviewDetail에서 이미지 URL 리스트를 가져와서 Review에 설정
        review.setSimpleEvaluation(reviewDetail.getSimpleEvaluation());
        review.setReviewDate(new Date()); // 리뷰 업데이트 날짜도 수정해줍니다.
        reviewRepository.save(review);
    }

//    private void saveUserRating(String userId, Long restaurantId, int rating) {
//        UserRating userRating = new UserRating();
//        userRating.setUserId(userId);
//        userRating.setRestaurantId(restaurantId);
//        userRating.setRating(rating);
//        userRatingRepository.save(userRating);
//    }
}