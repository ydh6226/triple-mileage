package com.triple.mileage.point.domain;

import com.triple.mileage.common.exception.BaseException;
import com.triple.mileage.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class PointException extends BaseException {

    public PointException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
