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
    private Long reviewId;
    private String userId;  // 리뷰 작성자의 아이디를 저장하기 위한 필드 추가
    private String reviewInfo;
    private int simpleEvaluation;
    private Date reviewDate;
    private List<String> reviewImage;

    // getters, setters, constructor 등
}