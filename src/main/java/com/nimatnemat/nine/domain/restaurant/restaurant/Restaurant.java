package com.nimatnemat.nine.domain.restaurant.restaurant;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurant_db")
@Getter
@NoArgsConstructor
public class Restaurant {
    @Id
    private Object _id;
    private Long restaurant_id;
    private String name;
    private String xPosition;
    private String yPosition;
    private String cuisineType;
    private Long avgPreference;
    private String address;
    private String imageFile;
}