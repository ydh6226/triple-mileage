package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.PointEvents;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.dto.PointModificationCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ContentPointRuleTest {

    private static final PointEvent ADD_CONTENT_EVENT = new PointEvent(UUID.randomUUID(), Reason.ADD_CONTENT, UUID.randomUUID(), UUID.randomUUID());
    private static final PointEvent OTHER_EVENT = new PointEvent(UUID.randomUUID(), Reason.ATTACH_PHOTO, UUID.randomUUID(), UUID.randomUUID());

    private static final UUID USER_ID = randomUUID();
    private static final UUID REVIEW_ID = randomUUID();
    private static final String VALID_CONTENT = "내용있다.";
    private static final String NULL_CONTENT = null;
    private static final List<UUID> PHOTO_IDS = Collections.singletonList(randomUUID());

    private final PointAdditionRule additionRule = new ContentPointRule();
    private final PointModificationRule modificationRule = new ContentPointRule();

    @DisplayName("텍스트가 blank 라면 포인트 생성 불가")
    @ParameterizedTest(name = "content: {0}")
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void notCreatable(String content) {
        // given
        PointAdditionCommand command = createCommand(content);

        // expect
        assertThat(additionRule.isCreatable(command)).isFalse();
    }

    @DisplayName("텍스트가 공백을 제외하고 1자 이상이라면 포인트 생성 가능")
    @Test
    void creatable() {
        // given
        String content = "내용";
        PointAdditionCommand command = createCommand(content);

        // expect
        assertThat(additionRule.isCreatable(command)).isTrue();
    }

    private PointAdditionCommand createCommand(String content) {
        return new PointAdditionCommand(randomUUID(), List.of(randomUUID()), randomUUID(), content, randomUUID());
    }

    @DisplayName("텍스트 추가 포인트 생성")
    @Test
    void create() {
        // given
        UUID userId = UUID.randomUUID();
        List<UUID> photoIds = Collections.emptyList();
        UUID placeId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        String content = "유효한 텍스트";

        PointAdditionCommand command = new PointAdditionCommand(userId, photoIds, placeId, content, reviewId);

        // when
        PointEvent pointEvent = additionRule.create(command);

        // then
        assertThat(pointEvent.getUserId()).isEqualTo(userId);
        assertThat(pointEvent.getReason()).isEqualTo(Reason.ADD_CONTENT);
        assertThat(pointEvent.getPlaceId()).isEqualTo(placeId);
        assertThat(pointEvent.getReviewId()).isEqualTo(reviewId);
    }

    @DisplayName("텍스트로 인한 포인트 수정이 필요한지")
    @ParameterizedTest(name = "[{index}] isModificationRequired: {2}")
    @MethodSource("isModificationRequiredSource")
    void isModificationRequired(PointEvents events, PointModificationCommand command, boolean isModificationRequired) {
        assertThat(modificationRule.isModificationRequired(events, command))
                .isEqualTo(isModificationRequired);
    }

    public static Stream<Arguments> isModificationRequiredSource() {
        return Stream.of(
                Arguments.of(
                        // 수정 전 텍스트 존재 o, 수정 후 텍스트 존재 o => 포인트 변경 없음.
                        PointEvents.activeEvents(List.of(ADD_CONTENT_EVENT)),
                        createModificationContent(VALID_CONTENT),
                        false
                ),
                Arguments.of(
                        // 수정 전 텍스트 존재 x, 수정 후 텍스트 존재 x => 포인트 변경 없음.
                        PointEvents.activeEvents(List.of(OTHER_EVENT)),
                        createModificationContent(NULL_CONTENT),
                        false
                ),
                Arguments.of(
                        // 수정 전 텍스트 존재 o, 수정 후 텍스트 존재 x => 포인트 변경 필요함.
                        PointEvents.activeEvents(List.of(ADD_CONTENT_EVENT)),
                        createModificationContent(NULL_CONTENT),
                        true
                ),
                Arguments.of(
                        // 수정 전 텍스트 존재 x, 수정 후 텍스트 존재 o => 포인트 변경 필요함.
                        PointEvents.activeEvents(List.of(OTHER_EVENT)),
                        createModificationContent(VALID_CONTENT),
                        true
                )
        );
    }

    private static PointModificationCommand createModificationContent(String content) {
        return new PointModificationCommand(USER_ID, content, PHOTO_IDS, REVIEW_ID);
    }

    private static UUID randomUUID() {
        return UUID.randomUUID();
    }

}
