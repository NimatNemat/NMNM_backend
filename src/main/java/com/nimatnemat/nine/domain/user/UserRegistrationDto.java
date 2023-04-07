package com.nimatnemat.nine.domain.user;

public class UserRegistrationDto {
    private String userId;

    private String password;
    private String email;
    private Long age;
    private String gender;
    private String nickName;
    private String profileImage;

    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Long getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getNickName() {
        return nickName;
    }


    public String getProfileImage() {
        return profileImage;
    }
    // Constructors, getters, setters, etc...
}

