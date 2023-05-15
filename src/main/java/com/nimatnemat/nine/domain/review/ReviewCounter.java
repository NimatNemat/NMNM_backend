package com.nimatnemat.nine.domain.review;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "review_counter")
@Getter
@Setter
public class ReviewCounter {
    @Id
    private String id; // 이 클래스는 단일 인스턴스만 필요하므로 id는 상수로 설정할 수 있습니다.
    private Long maxReviewId;

    public ReviewCounter() {
        this.id = "unique";
        this.maxReviewId = 0L;
    }

    public Long incrementAndGet() {
        this.maxReviewId += 1;
        return this.maxReviewId;
    }
}

