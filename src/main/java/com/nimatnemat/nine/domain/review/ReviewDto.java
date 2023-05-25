package com.nimatnemat.nine.domain.review;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {
    private Review review;

    public ReviewDto(Review review) {
        this.review = review;
    }

    // Getter와 Setter 메서드 생략

    // 필요한 경우 추가적인 메서드 및 로직을 구현할 수 있습니다.
}