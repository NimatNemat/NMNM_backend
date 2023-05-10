package com.nimatnemat.nine.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    private Long gender;
    private String nickName;
    private String email;
    private Date birthdate;
    // 생성자, getter, setter 생략
}

