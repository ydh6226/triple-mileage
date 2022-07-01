package com.triple.mileage.point.domain;

import com.triple.mileage.common.exception.ErrorCode;
import lombok.Getter;

@Getter
// TODO: 비즈니스예외 추상클래스 생성
public class PointException extends RuntimeException {

    private final ErrorCode errorCode;

    public PointException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
