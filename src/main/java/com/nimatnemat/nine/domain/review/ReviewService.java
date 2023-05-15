package com.nimatnemat.nine.domain.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(String userId, Long restaurantId, ReviewDetail reviewDetail) {
        // 유저 이름을 reviewDetail의 userId 필드에 설정

        // reviewId를 생성하는 로직이 필요하다면 여기에 구현하세요.
        // 예를 들어, reviewId를 데이터베이스에 현재 저장된 리뷰의 수에 기반하여 생성할 수 있습니다.
        Long reviewId = reviewRepository.count() + 1;

        // 새 Review 객체를 생성하고 필드를 설정
        Review review = new Review();
        reviewDetail.setReviewId(reviewId);
        reviewDetail.setUserId(userId);
        review.setRestaurantId(restaurantId); // restaurantId가 int형이므로 변환 필요
        review.getReviewDetail().add(reviewDetail); // 새로운 ReviewDetail을 추가

        // Review 객체를 데이터베이스에 저장하고 반환
        return reviewRepository.save(review);
    }
}