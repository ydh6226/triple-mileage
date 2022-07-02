package com.triple.mileage.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 서버에러
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR"),

    // 비즈니스에러
    ALREADY_WRITTEN_REVIEW("이미 작성된 리뷰가 있음."),
    NEGATIVE_ACCUMULATED_POINTS("누적포인트는 음수일 수 없음."),
    NO_VALID_POINT_EVENTS("유효한 포인트 내역 없음"),

    // 단순에러
    INVALID_PARAMETER("잘못된 파라미터입니다."),
    OPTIMISTIC_LOCK("OPTIMISTIC LOCK EXCEPTION 발생"),
    LOCK_ACQUIREMENT_FAIL("분산락 획득 실패");

    private final String description;
}
