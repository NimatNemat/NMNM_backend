package com.nimatnemat.nine.domain.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurantByRestaurantId(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId);
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