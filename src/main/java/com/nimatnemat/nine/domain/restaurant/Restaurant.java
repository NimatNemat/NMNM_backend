package com.nimatnemat.nine.domain.restaurant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurant_table")
@Getter
@NoArgsConstructor
public class Restaurant {
    @Id
    private Object _id;
    private Long restaurantId;
    private String name;
    private Long xPosition;
    private Long yPosition;
    private String cuisineType;
    private Long avgPreference;
    private String address;
    private String roadAddress;
    private String number;
    private String businessHours;
    private String tags;
    private Object imageFile;
}