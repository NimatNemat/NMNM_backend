package com.nimatnemat.nine.domain.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurantByRestaurantId(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId);
    }

    public void decreaseLikeCount(Long restaurantId, String userId) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findByRestaurantId(restaurantId);
        if (restaurantOpt.isPresent()) {
            Restaurant restaurant = restaurantOpt.get();
            List<String> likeUserList = restaurant.getLikeUserList();

            if (likeUserList.contains(userId)) {
                likeUserList.remove(userId);
                restaurant.setLikeUserList(likeUserList);
                restaurantRepository.save(restaurant);
            } else {
                // Handle the case when the user has not liked the restaurant
            }
        } else {
            // Handle the case when the restaurant is not found
        }
    }
    public Restaurant findByRestaurantId(Long restaurantId) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findByRestaurantId(restaurantId);
        return restaurantOpt.orElse(null);
    }
}


//package com.nimatnemat.nine.domain.restaurant;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class RestaurantService {
//    private final RestaurantRepository restaurantRepository;
//    public RestaurantNameSearchResponseDto getRestaurantList(String address) {
//        String Regex =".*" + address + ".*";
//        System.out.println(Regex);
//        List<Restaurant> restaurantList = restaurantRepository.findByNameRegex(Regex);
//
//        return new RestaurantNameSearchResponseDto(restaurantList);
//    }
//}