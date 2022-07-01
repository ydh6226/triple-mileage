package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ContentPointRuleTest {

    private final PointAdditionRule rule = new ContentPointRule();

    @DisplayName("텍스트가 blank 라면 포인트 생성 불가")
    @ParameterizedTest(name = "content: {0}")
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void notCreatable(String content) {
        // given
        PointAdditionCommand command = createCommand(content);

        // expect
        assertThat(rule.isCreatable(command)).isFalse();
    }

    @DisplayName("텍스트가 공백을 제외하고 1자 이상이라면 포인트 생성 가능")
    @Test
    void creatable() {
        // given
        String content = "내용";
        PointAdditionCommand command = createCommand(content);

        // expect
        assertThat(rule.isCreatable(command)).isTrue();
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
        PointEvent pointEvent = rule.create(command);

        // then
        assertThat(pointEvent.getUserId()).isEqualTo(userId);
        assertThat(pointEvent.getReason()).isEqualTo(Reason.ADD_CONTENT);
        assertThat(pointEvent.getPlaceId()).isEqualTo(placeId);
        assertThat(pointEvent.getReviewId()).isEqualTo(reviewId);
    }

    private static UUID randomUUID() {
        return UUID.randomUUID();
    }

}
