package com.triple.mileage.point.domain;

import com.triple.mileage.common.BaseCreatedTimeEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEvent extends BaseCreatedTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "binary(16)")
    private UUID id;

    @Column(columnDefinition = "binary(16)")
    private UUID reviewId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(30)")
    private Reason reason;

    @Column(columnDefinition = "binary(16)")
    private UUID userId;

    @Column(columnDefinition = "binary(16)")
    private UUID placeId;

    @Column(name = "mileage")
    private int value;

    public PointEvent(UUID reviewId, Reason reason, UUID userId, UUID placeId) {
        checkNotNull(reviewId, reason, userId, placeId);

        this.reviewId = reviewId;
        this.reason = reason;
        this.userId = userId;
        this.placeId = placeId;
        this.value = reason.getPoint();
    }

    private void checkNotNull(UUID reviewId, Reason reason, UUID userId, UUID placeId) {
        Assert.notNull(reviewId, "PointEvent.reviewId is required");
        Assert.notNull(reason, "PointEvent.reason is required");
        Assert.notNull(userId, "PointEvent.userId is required");
        Assert.notNull(placeId, "PointEvent.placeId is required");
    }
}
