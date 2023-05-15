package com.nimatnemat.nine.domain.review;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {
    List<Review> findByUserId(String userId);
    Review findByReviewId(Long reviewId);
    Review findByUserIdAndRestaurantId(String userId, Long restaurantId);
    void deleteByReviewId(Long reviewId);

}
