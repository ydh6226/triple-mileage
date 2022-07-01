package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoPointRuleTest {

    private static final String CONTENT = "내용";

    private final PointAdditionRule rule = new PhotoPointRule();

    @DisplayName("첨부된 사진이 없다면 포인트 추가 불가")
    @ParameterizedTest(name = "content: {0}")
    @NullAndEmptySource
    void notCreatable(List<UUID> photoIds) {
        // given
        PointAdditionCommand command = createCommand(photoIds);

        // expect
        assertThat(rule.isCreatable(command)).isFalse();
    }

    @DisplayName("첨부된 사진이 한개 이상이라며 포인트 추가 가능")
    @Test
    void creatable() {
        // given
        List<UUID> photoIds = List.of(randomUUID());
        PointAdditionCommand command = createCommand(photoIds);

        // expect
        assertThat(rule.isCreatable(command)).isTrue();
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
        PointEvent pointEvent = rule.create(command);

        // then
        assertThat(pointEvent.getUserId()).isEqualTo(userId);
        assertThat(pointEvent.getReason()).isEqualTo(Reason.ATTACH_PHOTO);
        assertThat(pointEvent.getPlaceId()).isEqualTo(placeId);
        assertThat(pointEvent.getReviewId()).isEqualTo(reviewId);
    }

    private static UUID randomUUID() {
        return UUID.randomUUID();
    }

}
