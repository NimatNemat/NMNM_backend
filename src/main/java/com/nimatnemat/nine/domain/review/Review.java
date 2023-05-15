package com.nimatnemat.nine.domain.review;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "review_table")
@Getter
@Setter
public class Review {
    @Id
    private ObjectId _id;
    private Long restaurantId;
    private List<ReviewDetail> reviewDetail;

    // 생성자에서 reviewDetail 필드를 빈 리스트로 초기화
    public Review() {
        this.reviewDetail = new ArrayList<>();
    }
    // getters, setters, constructor 등
}

