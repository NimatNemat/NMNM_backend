package com.nimatnemat.nine.domain.user;

import org.bson.types.ObjectId;

import java.util.Date;

public class UserRegistrationDto {
    private String userId;
    private Long gender;
    private String password;
    private String nickName;
    private String profileImage;
    private String email;
    private Date birthdate;
    public String getUserId() {
        return userId;
    }
    public Long getGender() {
        return gender;
    }
    public String getPassword() {
        return password;
    }
    public String getNickName() {
        return nickName;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public String getEmail() {return email;}
    public Date getBirthdate() {return birthdate;}
    // Constructors, getters, setters, etc...
}

