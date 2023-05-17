package com.nimatnemat.nine.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String userId;
    private Long gender;
    private String nickName;
    private String profileImage;
    private String email;
    private Date birthdate;
    private String groupName;
    private String infoMessage;


    public UserDto(User user) {
        this.userId = user.getUserId();
        this.gender = user.getGender();
        this.nickName = user.getNickName();
        this.profileImage = user.getProfileImage();
        this.email = user.getEmail();
        this.birthdate = user.getBirthdate();
        this.groupName = user.getGroupName();
        this.infoMessage = user.getInfoMessage();
    }
}
