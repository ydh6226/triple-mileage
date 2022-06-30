package com.triple.mileage.point.domain;

import com.triple.mileage.common.BaseCreatedTimeEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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
    private UUID id;

    private UUID reviewId;

    @Enumerated(EnumType.STRING)
    private Reason reason;

    private UUID userId;

    private UUID placeId;

    private int value;
}
