package com.nimatnemat.nine.domain.restaurant;

import com.nimatnemat.nine.domain.review.Review;
import com.nimatnemat.nine.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    @Autowired
    public ReviewRepository reviewRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    //하나의 아이디로 레스토랑 정보 조회
    public Optional<Restaurant> getRestaurantByRestaurantId(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId);
    }
    //여러개의 아이디에 대한 레스토랑 정보 조회
    public List<Restaurant> getRestaurantsByRestaurantIds(List<Long> restaurantIds) {
        List<Restaurant> restaurants = restaurantRepository.findByRestaurantIdIn(restaurantIds);
        // 원래 리스트의 순서대로 정렬
        restaurants.sort(Comparator.comparing(restaurant -> restaurantIds.indexOf(restaurant.getRestaurantId())));
        return restaurants;
    }

    public Restaurant findByRestaurantId(Long restaurantId) {
        Optional<Restaurant> restaurantOpt = restaurantRepository.findByRestaurantId(restaurantId);
        return restaurantOpt.orElse(null);
    }
    public String getRestaurantName(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findByRestaurantId(restaurantId);
        if (restaurantOptional.isPresent()) {
            Restaurant restaurant = restaurantOptional.get();
            return restaurant.getName();
        } else {
            throw new RuntimeException("식당을 찾을 수 없습니다.");
        }
    }
    public void updateAveragePreference(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        if (!reviews.isEmpty()) {
            double totalScore = 0;
            for (Review review : reviews) {
                totalScore += review.getReviewScore();
            }
            double averagePreference = totalScore / reviews.size();
            Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId).orElse(null);
            if (restaurant != null) {
                restaurant.setAvgPreference(averagePreference);
                restaurantRepository.save(restaurant);
            }
        }
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