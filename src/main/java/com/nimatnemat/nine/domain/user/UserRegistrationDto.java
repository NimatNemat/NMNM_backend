package com.nimatnemat.nine.domain.user;

import org.bson.types.ObjectId;

import java.util.Date;

public class UserRegistrationDto {
    private String userId;
    private String password;
    private Long gender;
    private String nickName;
    private String profileImage;
    private String email;
    private Date birthdate;
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public Long getGender() {
        return gender;
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

