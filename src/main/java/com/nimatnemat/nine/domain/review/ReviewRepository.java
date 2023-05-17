package com.nimatnemat.nine.domain.review;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByUserId(String userId);
    List<Review> findByRestaurantId(Long restaurantId);
    Optional<Review> findByReviewId(Long reviewId);  // 수정된 부분
    Review findByUserIdAndRestaurantId(String userId, Long restaurantId);
    void deleteByReviewId(Long reviewId);
}
