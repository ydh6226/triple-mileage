package com.triple.mileage.point.domain;

import com.triple.mileage.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.triple.mileage.point.domain.Reason.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PointEventsTest {

    private final static UUID REVIEW_ID = UUID.randomUUID();
    private final static UUID PLACE_ID = UUID.randomUUID();
    private final static UUID USER_ID = UUID.randomUUID();

    @DisplayName("삭제되지 않은 이벤트만 추출하기")
    @ParameterizedTest(name = "[{index}]")
    @MethodSource("activeEventsSource")
    void activeEvents(List<PointEvent> allEvents, List<PointEvent> activeEvents) {
        PointEvents pointEvents = PointEvents.activeEvents(allEvents);

        assertThat(pointEvents.getEvents()).isEqualTo(activeEvents);
    }

    public static Stream<Arguments> activeEventsSource() {
        return Stream.of(
                // 기존에 존재하던 이벤트가 하나도 없는 경우 유효한 이벤트 없음.
                Arguments.of(
                        Collections.emptyList(),
                        Collections.emptyList()
                        ),
                // 이벤트가 생성만 되고 보상은 없는 경우.
                Arguments.of(
                        List.of(createEvent(ATTACH_PHOTO)),
                        List.of(createEvent(ATTACH_PHOTO))
                        ),
                // 생성된 이벤트가 전부 보상된 경우 유효한 이벤트 없음.
                Arguments.of(
                        List.of(
                                createEvent(ATTACH_PHOTO),
                                createEvent(ADD_CONTENT),
                                createEvent(ADD_FIRST_REVIEW),
                                createEvent(DEL_CONTENT),
                                createEvent(DEL_FIRST_REVIEW),
                                createEvent(DETACH_PHOTO)
                        ),
                        Collections.emptyList()
                ),
                // 이벤트가 생성만 됐다가 전부 보상되고, 추가적인 이벤트가 생성된 경우.
                Arguments.of(
                        List.of(
                                createEvent(ATTACH_PHOTO),
                                createEvent(ADD_CONTENT),
                                createEvent(DETACH_PHOTO),
                                createEvent(DEL_CONTENT), // 보상 완료
                                createEvent(ADD_CONTENT) // 추가적으로 생성된 이벤트
                        ),
                        List.of(
                                createEvent(ADD_CONTENT)
                        )
                )
        );
    }

    @DisplayName("포인트 수정할 때 유요한 이벤트가 없다면 예외를 던진다.")
    @Test
    void modify_NoValidEvents_throwsException() {
        // given
        PointEvents pointEvents = PointEvents.activeEvents(Collections.emptyList());

        // expect
        assertThatExceptionOfType(PointException.class)
                .isThrownBy(() -> pointEvents.modify(ADD_CONTENT))
                .matches(e -> e.getErrorCode() == ErrorCode.NO_VALID_POINT_EVENTS);
    }

    @DisplayName("입력으로 받은 Reason의 이벤트가 존재하면 해당 이벤트를 보상하는 이벤트를 생성한다.")
    @ParameterizedTest(name = "originalReason: {0}, compensatedReason: {1}")
    @MethodSource("modifyCompensationSource")
    void modify_compensation(Reason originalReason, Reason compensatedReason) {
        // given
        PointEvents pointEvents = PointEvents.activeEvents(List.of(createEvent(originalReason)));

        // when
        PointEvent modifiedReason = pointEvents.modify(originalReason);

        // then
        assertThat(modifiedReason.getReason()).isEqualTo(compensatedReason);
    }

    public static Stream<Arguments> modifyCompensationSource() {
        return Stream.of(
                Arguments.of(ADD_CONTENT, DEL_CONTENT),
                Arguments.of(ATTACH_PHOTO, DETACH_PHOTO)
        );
    }

    @DisplayName("입력으로 받은 Reason의 이벤트가 존재하지 않으면 Reason에 해당하는 이벤트를 생성한다.")
    @Test
    void modify_addition() {
        // given
        PointEvent originalEvent = createEvent(ADD_CONTENT);

        PointEvents pointEvents = PointEvents.activeEvents(List.of(originalEvent));

        // when
        PointEvent modifiedReason = pointEvents.modify(ATTACH_PHOTO);

        // then
        assertThat(modifiedReason.getReason()).isEqualTo(ATTACH_PHOTO);
        assertThat(modifiedReason.getReviewId()).isEqualTo(originalEvent.getReviewId());
        assertThat(modifiedReason.getUserId()).isEqualTo(originalEvent.getUserId());
        assertThat(modifiedReason.getPlaceId()).isEqualTo(originalEvent.getPlaceId());
        assertThat(modifiedReason.getValue()).isEqualTo(originalEvent.getValue());
    }

    private static PointEvent createEvent(Reason reason) {
        return new PointEvent(REVIEW_ID, reason, USER_ID, PLACE_ID);
    }
}
