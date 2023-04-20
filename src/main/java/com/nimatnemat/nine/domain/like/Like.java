package com.nimatnemat.nine.domain.like;


import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "like_table")
@Getter
@Setter
@NoArgsConstructor
public class Like {
    @Id
    private ObjectId _id;
    private String userId;
    private Long restaurantId;

    // Constructors, getters, and setters
}
