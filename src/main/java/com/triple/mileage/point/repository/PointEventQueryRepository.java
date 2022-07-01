package com.triple.mileage.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static com.triple.mileage.point.domain.QPointEvent.pointEvent;

@Repository
@RequiredArgsConstructor
public class PointEventQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 해당 장소에 삭제 되지 않은 리뷰가 있는지
     */
    public boolean existsActiveReviewAt(UUID placeId) {
        Integer result = queryFactory
                .selectOne()
                .from(pointEvent)
                .where(pointEvent.placeId.eq(placeId))
                .groupBy(pointEvent.reviewId)
                .having(pointEvent.value.sum().gt(0)) // 리뷰포인트 점수의 합이 0 보다 크다면 유효한 리뷰가 있음을 의미함
                .fetchFirst();
        return result != null;
    }
}
