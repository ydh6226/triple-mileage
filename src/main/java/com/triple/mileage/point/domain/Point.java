package com.triple.mileage.point.domain;

import com.triple.mileage.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point extends BaseTimeEntity {

    @Id
    @Column(columnDefinition = "binary(16)")
    private UUID userId;

    @Column(name = "mileage")
    private int value;
}
