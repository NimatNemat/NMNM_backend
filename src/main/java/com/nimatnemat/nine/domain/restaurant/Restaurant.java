package com.nimatnemat.nine.domain.restaurant;

import com.nimatnemat.nine.domain.like.Like;
import jakarta.persistence.OneToMany;
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
    private Long xPosition;
    private Long yPosition;
    private String cuisineType;
    private Long avgPreference;
    private String address;
    private String roadAddress;
    private String number;
    private String businessHours;
    private List<List<String>> tags;
    @Field("imageFile")
    private ObjectId imageFile;
    private List<List<String>> menu;
    private String peculiarTaste;
    private Long likeCount;
    public String getImageFile() {
        if (imageFile != null) {
            return imageFile.toHexString();
        } else {
            // Handle the null case, e.g., log a warning, return a default value or throw a custom exception
            // For example, you can return null or an empty string:
            return null;
            // OR
            // return "";
        }
    }
}
