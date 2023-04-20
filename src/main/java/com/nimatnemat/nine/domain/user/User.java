package com.nimatnemat.nine.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Date;

@Document(collection = "user_table")
@Getter
@NoArgsConstructor
public class User {

    @Id
    private String _id;
    private String userId;
    private Long gender;
    private String password;
    private String nickName;
    @Field("profileImage")
    private byte[] profileImage;
    private String email;
    private Date birthdate;


    public User(String userId, Long gender, String password, String nickName,  byte[] profileImage, String email, Date birthdate) {
        this.userId = userId;
        this.gender = gender;
        this.password = password;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.email = email;
        this.birthdate = birthdate;
    }
    // Getters, setters, equals, hashCode, etc...
    public String getId() {
        return _id;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getEmail() {return email;}

    //    public void setAge(Long age) {
//        this.age = age;
//    }
}
