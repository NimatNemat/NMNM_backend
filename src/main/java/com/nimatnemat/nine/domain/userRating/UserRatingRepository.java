package com.nimatnemat.nine.domain.userRating;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// UserRatingRepository.java
@Repository
public interface UserRatingRepository extends MongoRepository<UserRating, ObjectId> {
    List<UserRating> findByUserId(String userId);
    List<UserRating> findByUserIdAndRestaurantId(String userId, Long restaurantId);
    void deleteByUserId(String userId);
}
