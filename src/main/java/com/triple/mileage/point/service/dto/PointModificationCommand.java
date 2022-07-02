package com.triple.mileage.point.service.dto;

import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Getter
public class PointModificationCommand {
    private final UUID userId;
    private final String content;
    private final List<UUID> photoIds;
    private final UUID reviewId;

    public PointModificationCommand(UUID userId, String content, List<UUID> photoIds, UUID reviewId) {
        checkValidReview(userId, content, photoIds, reviewId);

        this.userId = userId;
        this.content = content;
        this.photoIds = photoIds;
        this.reviewId = reviewId;
    }

    private void checkValidReview(UUID userId, String content, List<UUID> photoIds, UUID reviewId) {
        Assert.notNull(userId, "PointModificationCommand.userId is required");
        Assert.notNull(reviewId, "PointModificationCommand.reviewId is required");

        if (ObjectUtils.isEmpty(content) && ObjectUtils.isEmpty(photoIds)) {
            throw new IllegalArgumentException("텍스트와 첨부사진이 모두 비어있을 수는 없습니다");
        }
    }
}
