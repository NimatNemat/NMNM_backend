package com.nimatnemat.nine.domain.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    public RestaurantNameSearchResponseDto getRestaurantList(String name) {
        String regex = name;
        System.out.println(regex);
        List<Restaurant> restaurantList = restaurantRepository.findByNameRegex(regex);

        return new RestaurantNameSearchResponseDto(restaurantList);
    }
}
