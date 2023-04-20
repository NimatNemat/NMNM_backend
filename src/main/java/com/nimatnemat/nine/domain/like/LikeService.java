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

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantService restaurantService; // RestaurantService를 Autowire합니다.

    @Autowired
    private MongoTemplate mongoTemplate;

    // Other repositories and services needed for Restaurant and User

    public void likeRestaurant(String userId, Long restaurantId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndRestaurantId(userId, restaurantId);
        if (!existingLike.isPresent()) {
            Like like = new Like();
            like.setUserId(userId); // Set the userId for the Like object
            like.setRestaurantId(restaurantId); // Set the restaurantId for the Like object
            likeRepository.save(like);
            // Increase the like_count for the restaurant and save it
        }
    }


    public void unlikeRestaurant(String userId, Long restaurantId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndRestaurantId(userId, restaurantId);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            restaurantService.decreaseLikeCount(restaurantId); // 레스토랑의 like_count를 줄이고 저장합니다.
        }
    }

    public long getLikesForRestaurant(Long restaurantId) {
        // 아이디로 레스토랑 검색
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        // 특정 레스토랑에 대한 좋아요 목록 검색 및 반환
        return restaurant.getLikeCount();
    }
    public List<Long> getLikedRestaurantIdsForUser(String userId) {
        List<Like> likes = likeRepository.findByUserId(userId);
        List<Long> restaurantIds = new ArrayList<>();
        for (Like like : likes) {
            restaurantIds.add(like.getRestaurantId());
        }
        return restaurantIds;
    }
}
