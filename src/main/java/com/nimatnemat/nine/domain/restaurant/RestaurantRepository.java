package com.nimatnemat.nine.domain.restaurant;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {
    Optional<Restaurant> findByRestaurantId(Long restaurantId);
    List<Restaurant> findByRestaurantIdIn(List<Long> restaurantId);
    List<Restaurant> findByLikeUserListContaining(String userId);
}

