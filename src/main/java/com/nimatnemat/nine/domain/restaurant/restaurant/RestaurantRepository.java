package com.nimatnemat.nine.domain.restaurant.restaurant;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
    public List<Restaurant> findByNameRegex(String regex);
}

