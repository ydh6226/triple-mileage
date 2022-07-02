package com.triple.mileage.point.controller.dto;

import com.triple.mileage.point.domain.Point;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPointResponse {
    private final int mileage;

    public static UserPointResponse from(Point point) {
        return new UserPointResponse(point.getValue());
    }
}
