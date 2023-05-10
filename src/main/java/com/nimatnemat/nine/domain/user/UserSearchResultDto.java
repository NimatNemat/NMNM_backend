package com.nimatnemat.nine.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResultDto {
    private String userId;
    private String profileImage;
    private String nickName;
}
