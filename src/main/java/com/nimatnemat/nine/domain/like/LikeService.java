package com.nimatnemat.nine.domain.like;

import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.restaurant.RestaurantRepository;
import com.nimatnemat.nine.domain.restaurant.RestaurantService;
import com.nimatnemat.nine.global.config.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantService restaurantService; // RestaurantService를 Autowire합니다.
    @Autowired
    private MongoTemplate mongoTemplate;

    public void likeRestaurant(String userId, Long restaurantId) {
        Restaurant restaurant = restaurantService.findByRestaurantId(restaurantId);
        if (restaurant != null) {
            List<String> likedUserList = restaurant.getLikeUserList();
            if (likedUserList == null) {
                likedUserList = new ArrayList<>();
            }
            if (!likedUserList.contains(userId)) {
                likedUserList.add(userId);
                restaurant.setLikeUserList(likedUserList);
                restaurantRepository.save(restaurant);
            }
        }
    }

    public void unlikeRestaurant(String userId, Long restaurantId) {
        Restaurant restaurant = restaurantService.findByRestaurantId(restaurantId);
        if (restaurant != null) {
            List<String> likedUserList = restaurant.getLikeUserList();
            if (likedUserList == null) {
                likedUserList = new ArrayList<>();
            }
            if (likedUserList.contains(userId)) {
                likedUserList.remove(userId);
                restaurant.setLikeUserList(likedUserList);
                restaurantRepository.save(restaurant);
            }
        }
    }
    public void banRestaurant(String userId, Long restaurantId) {
        Restaurant restaurant = restaurantService.findByRestaurantId(restaurantId);
        if (restaurant != null) {
            List<String> bannedUserList = restaurant.getBanUserList();
            if (bannedUserList == null) {
                bannedUserList = new ArrayList<>();
            }
            if (!bannedUserList.contains(userId)) {
                bannedUserList.add(userId);
                restaurant.setBanUserList(bannedUserList);
                restaurantRepository.save(restaurant);
            }
        }
    }

    public void unbanRestaurant(String userId, Long restaurantId) {
        Restaurant restaurant = restaurantService.findByRestaurantId(restaurantId);
        if (restaurant != null) {
            List<String> bannedUserList = restaurant.getBanUserList();
            if (bannedUserList == null) {
                bannedUserList = new ArrayList<>();
            }
            if (bannedUserList.contains(userId)) {
                bannedUserList.remove(userId);
                restaurant.setBanUserList(bannedUserList);
                restaurantRepository.save(restaurant);
            }
        }
    }

    public List<Long> getLikedRestaurantIdsForUser(String userId) {
        List<Restaurant> likedRestaurants = restaurantRepository.findByLikeUserListContaining(userId);
        return likedRestaurants.stream().map(Restaurant::getRestaurantId).collect(Collectors.toList());
    }
}


// Other repositories and services needed for Restaurant and User

//    public void likeRestaurant(String userId, Long restaurantId) {
//        Optional<Like> existingLike = likeRepository.findByUserIdAndRestaurantId(userId, restaurantId);
//        if (!existingLike.isPresent()) {
//            Like like = new Like();
//            like.setUserId(userId); // Set the userId for the Like object
//            like.setRestaurantId(restaurantId); // Set the restaurantId for the Like object
//            likeRepository.save(like);
//            // Increase the like_count for the restaurant and save it
//        }
//    }
//
//
//    public void unlikeRestaurant(String userId, Long restaurantId) {
//        Optional<Like> existingLike = likeRepository.findByUserIdAndRestaurantId(userId, restaurantId);
//        if (existingLike.isPresent()) {
//            likeRepository.delete(existingLike.get());
//            restaurantService.decreaseLikeCount(restaurantId); // 레스토랑의 like_count를 줄이고 저장합니다.
//        }
//    }
