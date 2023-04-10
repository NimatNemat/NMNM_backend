package com.nimatnemat.nine.domain.restaurant;

import com.nimatnemat.nine.domain.restaurant.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RestaurantNameSearchResponseDto {
    private List<Restaurant> restaurant;

    public RestaurantNameSearchResponseDto(List<Restaurant> list) {
        this.restaurant = list;
    }
}
