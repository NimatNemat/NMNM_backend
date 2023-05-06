//package com.nimatnemat.nine.domain.like;
//
//import com.nimatnemat.nine.domain.like.Like;
//import com.nimatnemat.nine.domain.restaurant.Restaurant;
//import com.nimatnemat.nine.domain.user.User;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface LikeRepository extends MongoRepository<Like, ObjectId> {
//    Optional<Like> findByRestaurantId(Long restaurantId);
//    List<Like> findByUserId(String userId);
//    // Remove the findRestaurantIdByUserId method
//}
