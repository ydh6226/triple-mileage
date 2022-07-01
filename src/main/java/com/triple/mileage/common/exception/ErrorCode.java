package com.triple.mileage.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 서버에러
    INTERNAL_SERVER_ERROR("잠시 후 다시 요청하세요."),

    // 비즈니스에러
    ALREADY_WRITTEN_REVIEW("이미 작성된 리뷰가 있음."),
    NEGATIVE_ACCUMULATED_POINTS("누적포인트는 음수일 수 없음."),

    // 단순에러
    INVALID_PARAMETER("잘못된 파라미터입니다.");

    private final String description;
}
