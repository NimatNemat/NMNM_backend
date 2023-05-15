package com.nimatnemat.nine.domain.review;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, Long> {
    List<Review> findByReviewDetailUserId(String userId);
    Review findByReviewDetailReviewId(Long reviewId);
}