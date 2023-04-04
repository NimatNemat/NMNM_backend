package com.nimatnemat.nine.domain.restaurant.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("api/restaurant/{name}")
    public RestaurantNameSearchResponseDto findByName(@PathVariable String name){
        System.out.println("name"+name);
        return restaurantService.getRestaurantList(name);
        // return (ShelterNameSearchResponsseDto) shelterService.getShelterList(title);
    }
}
