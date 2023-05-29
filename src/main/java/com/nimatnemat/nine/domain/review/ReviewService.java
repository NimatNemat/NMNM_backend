package com.nimatnemat.nine.domain.review;

import com.mongodb.client.gridfs.GridFSBucket;
import com.nimatnemat.nine.domain.userRating.UserRating;
import com.nimatnemat.nine.domain.userRating.UserRatingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private  ReviewRepository reviewRepository;
    @Autowired
    private GridFSBucket gridFsBucket;
    @Autowired
    private GridFsTemplate gridFsTemplate; // GridFS를 사용하기 위한 의존성 주입
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ReviewCounterRepository reviewCounterRepository;
    @Autowired
    private UserRatingRepository userRatingRepository;  // user_rating_table에 접근하기 위한 repository

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
            String imageUrl = String.format("/images/%s", objectId.toString());
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
        updateUserRating(userId, restaurantId, reviewDetail.getReviewScore());// 리뷰가 성공적으로 생성된 후에 사용자의 레스토랑 평점을 업데이트합니다.
        review.setSimpleEvaluation(reviewDetail.getSimpleEvaluation());
        review.setReviewDate(new Date());

        // Review 객체를 데이터베이스에 저장하고 반환
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findByReviewId(reviewId);
        if(reviewOptional.isPresent()){
            Review review = reviewOptional.get();
            List<String> reviewImages = review.getReviewImage();

            // 리뷰에 연결된 모든 이미지를 삭제
            for(String imageUrl : reviewImages){
                String imageId = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
                // ObjectId를 사용하여 GridFS에서 이미지 삭제
                gridFsTemplate.delete(new Query(Criteria.where("_id").is(new ObjectId(imageId))));
                mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(imageId))), "fs.files");
            }

            // 리뷰를 삭제
            reviewRepository.deleteByReviewId(reviewId);
        }
    }
    public void updateReview(Long reviewId, ReviewDetail reviewDetail) {
        Optional<Review> reviewOptional = reviewRepository.findByReviewId(reviewId);
        if (!reviewOptional.isPresent()) {
            throw new RuntimeException("해당하는 리뷰가 없습니다.");
        }
        Review review = reviewOptional.get();
        review.setReviewInfo(reviewDetail.getReviewInfo());
        review.setReviewScore(reviewDetail.getReviewScore());
        review.setReviewImage(reviewDetail.getReviewImage()); // ReviewDetail에서 이미지 URL 리스트를 가져와서 Review에 설정
        review.setSimpleEvaluation(reviewDetail.getSimpleEvaluation());
        review.setReviewDate(new Date()); // 리뷰 업데이트 날짜도 수정해줍니다.
        reviewRepository.save(review);
        updateUserRating(review.getUserId(), review.getRestaurantId(), reviewDetail.getReviewScore());
    }
    public void updateUserRating(String userId, Long restaurantId, int newRating) {
        UserRating userRating = userRatingRepository.findByUserIdAndRestaurantId(userId, restaurantId);
        if (userRating != null) {
            userRating.setRating(newRating);
            userRatingRepository.save(userRating);
        } else {
            // 해당하는 UserRating이 없는 경우에 대한 처리 코드를 여기에 추가할 수 있습니다.
        }
    }
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 리뷰가 존재하지 않습니다."));
    }
//    private void saveUserRating(String userId, Long restaurantId, int rating) {
//        UserRating userRating = new UserRating();
//        userRating.setUserId(userId);
//        userRating.setRestaurantId(restaurantId);
//        userRating.setRating(rating);
//        userRatingRepository.save(userRating);
//    }
}