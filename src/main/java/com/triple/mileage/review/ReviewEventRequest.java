package com.triple.mileage.review;

import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.dto.PointModificationCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@NoArgsConstructor
public class ReviewEventRequest {

    @NotNull
    private Type type;

    @NotNull
    private Action action;

    @NotNull
    private UUID reviewId;

    private List<UUID> attachedPhotoIds;

    public String content;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID placeId;

    public enum Type {
        REVIEW
    }

    public enum Action {
        ADD,
        MOD,
        DELETE
    }

    public PointAdditionCommand toAdditionCommand() {
        return new PointAdditionCommand(
                userId,
                attachedPhotoIds,
                placeId,
                content,
                reviewId
        );
    }

    public PointModificationCommand toModificationCommand() {
        return new PointModificationCommand(
                userId,
                content,
                attachedPhotoIds,
                reviewId
        );
    }
}
