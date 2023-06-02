package com.nimatnemat.nine.domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDetail {
    private String reviewInfo;
    private int reviewScore;
    private int simpleEvaluation;
    private Date reviewDate;
    private List<String> reviewImage;
    private String userNickName;
    private String restaurantName;
    private String profileImage;



    // getters, setters, constructor 등
}