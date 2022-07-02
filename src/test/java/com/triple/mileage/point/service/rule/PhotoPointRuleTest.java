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

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoPointRuleTest {

    private static final PointEvent ATTACH_PHOTO_EVENT = new PointEvent(UUID.randomUUID(), Reason.ATTACH_PHOTO, UUID.randomUUID(), UUID.randomUUID());
    private static final PointEvent OTHER_EVENT = new PointEvent(UUID.randomUUID(), Reason.ADD_CONTENT, UUID.randomUUID(), UUID.randomUUID());

    private static final UUID USER_ID = randomUUID();
    private static final UUID REVIEW_ID = randomUUID();
    private static final List<UUID> VALID_PHOTO_IDS = Collections.singletonList(randomUUID());
    private static final List<UUID> NULL_PHOTO_IDS = Collections.emptyList();
    private static final String CONTENT = "내용";

    private final PointAdditionRule additionRule = new PhotoPointRule();
    private final PointModificationRule modificationRule = new PhotoPointRule();

    @DisplayName("첨부된 사진이 없다면 포인트 추가 불가")
    @ParameterizedTest(name = "content: {0}")
    @NullAndEmptySource
    void notCreatable(List<UUID> photoIds) {
        // given
        PointAdditionCommand command = createCommand(photoIds);

        // expect
        assertThat(additionRule.isCreatable(command)).isFalse();
    }

    @DisplayName("첨부된 사진이 한개 이상이라면 포인트 추가 가능")
    @Test
    void creatable() {
        // given
        List<UUID> photoIds = List.of(randomUUID());
        PointAdditionCommand command = createCommand(photoIds);

        // expect
        assertThat(additionRule.isCreatable(command)).isTrue();
    }

    private PointAdditionCommand createCommand(List<UUID> photoIds) {
        return new PointAdditionCommand(randomUUID(), photoIds, randomUUID(), CONTENT, randomUUID());
    }

    @DisplayName("사진 추가 포인트 생성")
    @Test
    void create() {
        // given
        UUID userId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        List<UUID> photoIds = List.of(randomUUID());

        PointAdditionCommand command = new PointAdditionCommand(userId, photoIds, placeId, CONTENT, reviewId);

        // when
        PointEvent pointEvent = additionRule.create(command);

        // then
        assertThat(pointEvent.getUserId()).isEqualTo(userId);
        assertThat(pointEvent.getReason()).isEqualTo(Reason.ATTACH_PHOTO);
        assertThat(pointEvent.getPlaceId()).isEqualTo(placeId);
        assertThat(pointEvent.getReviewId()).isEqualTo(reviewId);
    }

    @DisplayName("사진으로 인한 포인트 수정이 필요한지")
    @ParameterizedTest(name = "[{index}] isModificationRequired: {2}")
    @MethodSource("isModificationRequiredSource")
    void isModificationRequired(PointEvents events, PointModificationCommand command, boolean isModificationRequired) {
        assertThat(modificationRule.isModificationRequired(events, command))
                .isEqualTo(isModificationRequired);
    }

    public static Stream<Arguments> isModificationRequiredSource() {
        return Stream.of(
                Arguments.of(
                        // 수정 전 사진 존재 o, 수정 후 사진 존재 o => 포인트 변경 없음.
                        PointEvents.activeEvents(List.of(ATTACH_PHOTO_EVENT)),
                        createModificationCommand(VALID_PHOTO_IDS),
                        false
                ),
                Arguments.of(
                        // 수정 전 사진 존재 x, 수정 후 사진 존재 x => 포인트 변경 없음.
                        PointEvents.activeEvents(List.of(OTHER_EVENT)),
                        createModificationCommand(NULL_PHOTO_IDS),
                        false
                ),
                Arguments.of(
                        // 수정 전 사진 존재 x, 수정 후 사진 존재 o => 포인트 변경 필요함.
                        PointEvents.activeEvents(List.of(OTHER_EVENT)),
                        createModificationCommand(VALID_PHOTO_IDS),
                        true
                ),
                Arguments.of(
                        // 수정 전 사진 존재 o, 수정 후 사진 존재 x => 포인트 변경 필요함.
                        PointEvents.activeEvents(List.of(ATTACH_PHOTO_EVENT)),
                        createModificationCommand(NULL_PHOTO_IDS),
                        true
                )
        );
    }

    private static PointModificationCommand createModificationCommand(List<UUID> photoIds) {
        return new PointModificationCommand(USER_ID, CONTENT, photoIds, REVIEW_ID);
    }

    private static UUID randomUUID() {
        return UUID.randomUUID();
    }

}
