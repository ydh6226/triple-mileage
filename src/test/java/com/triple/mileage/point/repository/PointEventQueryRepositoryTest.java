package com.triple.mileage.point.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class PointEventQueryRepositoryTest {

    private static final UUID PLACE_ID = UUID.randomUUID();
    private static final UUID REVIEW_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();

    @Autowired
    EntityManager entityManager;

    @Autowired
    PointEventRepository pointEventRepository;

    PointEventQueryRepository queryRepository;

    @BeforeEach
    public void init() {
        queryRepository = new PointEventQueryRepository(new JPAQueryFactory(entityManager));
    }

    @DisplayName("해당 장소에 리뷰가 하나도 없는 경우 유효한 리뷰는 없다.")
    @Test
    void existsActiveReviewAt() {
        assertThat(queryRepository.existsActiveReviewAt(PLACE_ID)).isFalse();
    }

    @DisplayName("해당 장소에 이미 작성된 리뷰가 있는 경우 유효한 리뷰가 있다.")
    @Test
    void existsActiveReviewAt2() {
        //given
        pointEventRepository.save(createEvent(Reason.ADD_CONTENT));
        pointEventRepository.save(createEvent(Reason.ADD_FIRST_REVIEW));
        flushAndClear();

        //expect
        assertThat(queryRepository.existsActiveReviewAt(PLACE_ID)).isTrue();
    }

    @DisplayName("해당 장소에 이미 작성된 리뷰가 있지만 전부 보상된 경우 유효한 리뷰는 없다.")
    @Test
    void existsActiveReviewAt3() {
        //given
        pointEventRepository.save(createEvent(Reason.ADD_CONTENT));
        pointEventRepository.save(createEvent(Reason.DEL_CONTENT));
        pointEventRepository.save(createEvent(Reason.ADD_FIRST_REVIEW));
        pointEventRepository.save(createEvent(Reason.DEL_FIRST_REVIEW));
        flushAndClear();

        //expect
        assertThat(queryRepository.existsActiveReviewAt(PLACE_ID)).isFalse();
    }

    private PointEvent createEvent(Reason reason) {
        return new PointEvent(REVIEW_ID, reason, USER_ID, PLACE_ID);
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

}
