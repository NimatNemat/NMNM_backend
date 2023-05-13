package com.nimatnemat.nine.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Document(collection = "user_table")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private ObjectId _id;
    private String userId;
    private Long gender;
    private String password;
    private String nickName;
    private String profileImage;
    private String email;
    private Date birthdate;
    private String groupName;


    public User(String userId, Long gender, String password, String nickName,  String profileImage, String email, Date birthdate, String groupName) {
        this.userId = userId;
        this.gender = gender;
        this.password = password;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.email = email;
        this.birthdate = birthdate;
        this.groupName = groupName;
    }
}
