package com.triple.mileage.point.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PointEventTest {

    private static final UUID REVIEW_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID PLACE_ID = UUID.randomUUID();

    @DisplayName("해당 이벤트를 보상하는 이벤트를 생성한다. Reason만 반대로하고 나머지 값은 그대로 유지한다.")
    @ParameterizedTest(name = "origin: {0}, compensation: {1}")
    @CsvSource({
            "ATTACH_PHOTO, DETACH_PHOTO",
            "ADD_CONTENT, DEL_CONTENT"
    })
    void compensate(Reason origin, Reason compensation) {
        PointEvent event = new PointEvent(REVIEW_ID, origin, USER_ID, PLACE_ID);

        assertThat(event.compensate()).isEqualTo(new PointEvent(REVIEW_ID, compensation, USER_ID, PLACE_ID));
    }

}
