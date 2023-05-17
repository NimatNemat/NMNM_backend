package com.nimatnemat.nine.domain.follow;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Document(collection = "follow_table")
@Getter
@Setter
@NoArgsConstructor
public class Follow {
    @Id
    private ObjectId _id;
    private String followId;
    private String followingId;

    public Follow(String followId, String followingId){
        this.followId = followId;
        this.followingId = followingId;
    }
}