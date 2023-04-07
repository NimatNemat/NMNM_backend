package com.nimatnemat.nine.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "user_db")
@Getter
@NoArgsConstructor
public class User {

    @Id
    private String _id;
    private String userId;
    private Long age;
    private String gender;
    private String password;
    private String nickName;
    private String email;
    private String profileImage;

    public User(String userId, Long age, String gender, String password, String nickName, String email, String profileImage) {
        this.userId = userId;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
    }

    // Getters, setters, equals, hashCode, etc...
}
