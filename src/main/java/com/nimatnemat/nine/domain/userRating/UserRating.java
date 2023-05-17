package com.nimatnemat.nine.domain.userRating;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Document(collection = "user_rating_table")
@Getter
@Setter
@NoArgsConstructor
public class UserRating {
    @Id
    private ObjectId _id;
    private String userId;
    private Long restaurantId;
    private int rating;
}


