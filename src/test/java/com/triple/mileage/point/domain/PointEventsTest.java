package com.triple.mileage.point.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.triple.mileage.point.domain.Reason.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    private static PointEvent createEvent(Reason reason) {
        return new PointEvent(REVIEW_ID, reason, USER_ID, PLACE_ID);
    }
}
