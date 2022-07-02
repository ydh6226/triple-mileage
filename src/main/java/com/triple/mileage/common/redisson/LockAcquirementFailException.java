package com.triple.mileage.common.redisson;

import com.triple.mileage.common.exception.BaseException;
import com.triple.mileage.common.exception.ErrorCode;

public class LockAcquirementFailException extends BaseException {

    private static final ErrorCode errorCode = ErrorCode.LOCK_ACQUIREMENT_FAIL;

    public LockAcquirementFailException(String message) {
        super(errorCode, message);
    }
}
