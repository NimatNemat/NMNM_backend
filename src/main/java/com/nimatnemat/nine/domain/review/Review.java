package com.nimatnemat.nine.domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "review_table")
@Getter
@Setter
public class Review {
    @Id
    private ObjectId _id;
    private Long reviewId;
    private Long restaurantId;
    private String userId;  // 리뷰 작성자의 아이디를 저장하기 위한 필드 추가
    private String reviewInfo;
    private int simpleEvaluation;
    private Date reviewDate;
    private List<String> reviewImage;

    // 생성자에서 reviewDetail 필드를 빈 리스트로 초기화
//    public Review() {
//        this.reviewDetail = new ArrayList<>();
//    }
    // getters, setters, constructor 등
}

