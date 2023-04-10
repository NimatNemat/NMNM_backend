package com.nimatnemat.nine.domain.restaurant;

import com.nimatnemat.nine.domain.restaurant.Restaurant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    List<Restaurant> findByNameRegex(String Regex);
}

