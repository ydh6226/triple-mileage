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
                .having(pointEvent.value.sum().gt(0))
                .fetchFirst();
        return result != null;
    }
}
