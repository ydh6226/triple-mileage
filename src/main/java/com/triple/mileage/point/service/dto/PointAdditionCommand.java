package com.triple.mileage.point.service.dto;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Getter
public class PointAdditionCommand {

    private final UUID userId;
    private final UUID placeId;
    private final UUID reviewId;
    private final List<UUID> photoIds;
    private final String content;

    public PointAdditionCommand(UUID userId, List<UUID> photoIds, UUID placeId, String content, UUID reviewId) {
        checkValidReview(userId, photoIds, placeId, content, reviewId);
        this.userId = userId;
        this.placeId = placeId;
        this.reviewId = reviewId;
        this.photoIds = photoIds;
        this.content = content;
    }

    private void checkValidReview(UUID userId, List<UUID> photoIds, UUID placeId, String content, UUID reviewId) {
        Assert.notNull(userId, "PointAdditionCommand.userId is required");
        Assert.notNull(placeId, "PointAdditionCommand.placeId is required");
        Assert.notNull(reviewId, "PointAdditionCommand.reviewId is required");

        if (ObjectUtils.isEmpty(content) && ObjectUtils.isEmpty(photoIds)) {
            throw new IllegalArgumentException("텍스트와 첨부사진이 모두 비어있을 수는 없습니다");
        }
    }

    public PointEvent toEntity(Reason reason) {
        Assert.notNull(reason, "reason is required");

        return new PointEvent(reviewId, reason, userId, placeId);
    }
}
