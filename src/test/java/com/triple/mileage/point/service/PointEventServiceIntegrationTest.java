package com.triple.mileage.point.service;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.repository.PointEventRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PointEventServiceIntegrationTest {

    private static final UUID USER_A_ID = UUID.randomUUID();
    private static final UUID REVIEW_A_ID = UUID.randomUUID();

    private static final UUID USER_B_ID = UUID.randomUUID();
    private static final UUID REVIEW_B_ID = UUID.randomUUID();

    private static final UUID PLACE_ID = UUID.randomUUID();
    private static final String CONTENT = "내용";

    @Autowired
    PointEventService pointEventService;

    @Autowired
    PointEventRepository pointEventRepository;

    @DisplayName("유저 관점에서 첫 번째 리뷰만 보너스 포인트를 받는다.")
    @Test
    void firstPlacePointTest() {
        // given
        // userA: 첫 리뷰 포인트 + 텍스트 포인트
        PointAdditionCommand userAaaCommand = new PointAdditionCommand(
                USER_A_ID,
                Collections.emptyList(),
                PLACE_ID,
                CONTENT,
                REVIEW_A_ID
        );

        // userB: 텍스트 포인트
        PointAdditionCommand userBbbCommand = new PointAdditionCommand(
                USER_B_ID,
                Collections.emptyList(),
                PLACE_ID,
                CONTENT,
                REVIEW_B_ID
        );

        // when
        // 유저A 리뷰 남김
        pointEventService.add(userAaaCommand);

        // 유저B 리뷰 남김
        pointEventService.add(userBbbCommand);

        // 유저A 리뷰 삭제
        pointEventService.withdraw(REVIEW_A_ID);

        // then
        List<PointEvent> userBbbEvents = pointEventRepository.findByUserIdOrderByCreatedDateDesc(USER_B_ID);

        // 리뷰 작성한 시점에 첫 리뷰가 아니기 때문에 첫 리뷰 포인트 없음.
        assertThat(userBbbEvents).containsExactlyInAnyOrder(
                new PointEvent(REVIEW_B_ID, Reason.ADD_CONTENT, USER_B_ID, PLACE_ID)
        );
    }

}
