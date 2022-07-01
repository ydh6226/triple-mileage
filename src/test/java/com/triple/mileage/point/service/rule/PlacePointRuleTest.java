package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.repository.PointEventQueryRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PlacePointRuleTest {

    private static final String CONTENT = "내용";

    @InjectMocks
    private PlacePointRule rule;

    @Mock
    private PointEventQueryRepository pointEventQueryRepository;

    @DisplayName("장소 첫 리뷰 포인트 생성")
    @Test
    void create() {
        // given
        UUID userId = UUID.randomUUID();
        UUID placeId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();
        List<UUID> photoIds = Collections.emptyList();

        PointAdditionCommand command = new PointAdditionCommand(userId, photoIds, placeId, CONTENT, reviewId);

        // when
        PointEvent pointEvent = rule.create(command);

        // then
        assertThat(pointEvent.getUserId()).isEqualTo(userId);
        assertThat(pointEvent.getReason()).isEqualTo(Reason.ADD_FIRST_REVIEW);
        assertThat(pointEvent.getPlaceId()).isEqualTo(placeId);
        assertThat(pointEvent.getReviewId()).isEqualTo(reviewId);
    }

    private static UUID randomUUID() {
        return UUID.randomUUID();
    }
}
