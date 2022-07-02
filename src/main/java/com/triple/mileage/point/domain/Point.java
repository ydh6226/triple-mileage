package com.triple.mileage.point.domain;

import com.triple.mileage.common.entity.BaseTimeEntity;
import com.triple.mileage.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.text.MessageFormat;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseTimeEntity {

    private static final int MIN_VALUE = 0;

    @Id
    @Column(columnDefinition = "binary(16)")
    private UUID userId;

    @Column(name = "mileage")
    private int value;

    public Point(UUID userId) {
        this(userId, MIN_VALUE);
    }

    public Point(UUID userId, int value) {
        Assert.notNull(userId, "Point.userId is required");
        checkNotNegative(value);

        this.userId = userId;
        this.value = value;
    }

    private void checkNotNegative(int value) {
        if (value < MIN_VALUE) {
            throw new PointException(ErrorCode.NEGATIVE_ACCUMULATED_POINTS, MessageFormat.format("포인트는 {0} 이상이어야 합니다", MIN_VALUE));
        }
    }

    public void sum(int value) {
        this.value += value;
    }
}
