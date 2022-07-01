package com.triple.mileage.point.service;

import com.triple.mileage.point.domain.Point;
import com.triple.mileage.point.repository.PointRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    private static final UUID USER_ID = randomUUID();
    private static final UUID REVIEW_ID = randomUUID();
    private static final String CONTENT = "내용";
    private static final int ADDITIONAL_POINT = 5;

    @InjectMocks
    PointService pointService;

    @Mock
    PointRepository pointRepository;

    @Mock
    PointEventService pointEventService;

    @Captor
    private ArgumentCaptor<Point> captor;

    @DisplayName("userId로 Point가 조회된 경우 해당 Point에 값을 더한다.")
    @Test
    void add() {
        // given
        Point point = new Point(USER_ID);

        given(pointRepository.findById(USER_ID))
                .willReturn(Optional.of(point));

        PointAdditionCommand command = createCommand();
        given(pointEventService.add(command)).willReturn(ADDITIONAL_POINT);

        // when
        pointService.add(command);

        // then
        assertThat(point.getValue()).isEqualTo(ADDITIONAL_POINT);
    }

    @DisplayName("userId로 Point가 조회되지 않은 경우 새로운 Point를 생성하고 값을 더한다.")
    @Test
    void add2() {
        // given
        given(pointRepository.findById(USER_ID))
                .willReturn(Optional.empty());

        PointAdditionCommand command = createCommand();
        given(pointEventService.add(command)).willReturn(ADDITIONAL_POINT);

        // when
        pointService.add(command);

        // then
        verify(pointRepository).save(captor.capture());

        Point captorPoint = captor.getValue();
        assertThat(captorPoint.getValue()).isEqualTo(ADDITIONAL_POINT);
    }

    @DisplayName("리뷰 삭제로 인한 포인트 회수")
    @Test
    void withdrawReviewPoint() {
        int originPoint = 10;
        Point point = new Point(USER_ID, originPoint);

        given(pointRepository.findById(USER_ID))
                .willReturn(Optional.of(point));

        int changedPoint = -2;
        given(pointEventService.withdraw(REVIEW_ID)).willReturn(changedPoint);

        // when
        pointService.withdrawReviewPoint(USER_ID, REVIEW_ID);

        // then
        assertThat(point.getValue()).isEqualTo(8);
    }

    private PointAdditionCommand createCommand() {
        return new PointAdditionCommand(USER_ID, Collections.emptyList(), randomUUID(), CONTENT, REVIEW_ID);
    }

    private static UUID randomUUID() {
        return UUID.randomUUID();
    }

}
