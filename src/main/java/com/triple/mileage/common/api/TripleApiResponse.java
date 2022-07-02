package com.triple.mileage.common.api;

import com.triple.mileage.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TripleApiResponse<T> {

    private final Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

    private final boolean success;

    private final T data;

    private final ErrorCode errorCode;

    public static <T> TripleApiResponse<T> ok() {
        return ok(null);
    }

    public static <T> TripleApiResponse<T> ok(T data) {
        return new TripleApiResponse<>(true, data, null);
    }

    public static <T> TripleApiResponse<T> fail(ErrorCode errorCode) {
        return fail(null, errorCode);
    }

    public static <T> TripleApiResponse<T> fail(T data, ErrorCode errorCode) {
        return new TripleApiResponse<>(false, data, errorCode);
    }

}
