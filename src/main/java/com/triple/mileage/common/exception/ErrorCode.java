package com.triple.mileage.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALREADY_WRITTEN_REVIEW("이미 작성된 리뷰가 있음.");

    private final String description;
}
