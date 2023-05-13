package com.nimatnemat.nine.domain.restaurant;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "restaurant_table")
@Getter
@Setter
@NoArgsConstructor
public class Restaurant {
    @Id
    private ObjectId _id;
    private Long restaurantId;
    private String name;
    private Double xPosition;
    private Double yPosition;
    private String cuisineType;
    private Double avgPreference;
    private String address;
    private String roadAddress;
    private String number;
    private String businessHours;
    private List<List<String>> tags;
    @Field("imageFile")
    private ObjectId imageFile;
    private List<List<String>> menu;
    private String peculiarTaste;
    private List<String> likeUserList;
    private List<String> banUserList;
    @Transient
    private String imageUrl;
}
