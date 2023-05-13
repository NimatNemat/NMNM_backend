package com.nimatnemat.nine.domain.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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