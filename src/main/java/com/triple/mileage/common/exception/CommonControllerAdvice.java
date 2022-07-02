package com.triple.mileage.common.exception;

import com.triple.mileage.common.api.TripleApiResponse;
import com.triple.mileage.common.lock.LockAcquirementFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    private static final String RETRY_MESSAGE = "잠시 후 다시 시도하세요.";

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public TripleApiResponse<Void> handle(Exception e) {
        log.error("예상하지 못한 예외 발생: {}", e.getMessage(), e);
        return TripleApiResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public TripleApiResponse<String> handle(BaseException e) {
        log.info("비즈니스 예외 발생: {}", e.getErrorCode(), e);
        return TripleApiResponse.fail(e.getMessage(), e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public TripleApiResponse<String> handleBadRequest(IllegalArgumentException e) {
        log.info("잘못된 파라미터: {}", e.getMessage(), e);
        return TripleApiResponse.fail(e.getMessage(), ErrorCode.INVALID_PARAMETER);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public TripleApiResponse<String> handleBadRequest(ObjectOptimisticLockingFailureException e) {
        log.info(ErrorCode.OPTIMISTIC_LOCK.getDescription(), e);
        return TripleApiResponse.fail(RETRY_MESSAGE, ErrorCode.OPTIMISTIC_LOCK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public TripleApiResponse<String> handleBadRequest(LockAcquirementFailException e) {
        log.info(ErrorCode.LOCK_ACQUIREMENT_FAIL.getDescription(), e);
        return TripleApiResponse.fail(RETRY_MESSAGE, ErrorCode.LOCK_ACQUIREMENT_FAIL);
    }

    // Bean Validation
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public TripleApiResponse<HashMap<String, String>> handleMethodArgumentNotValidException(BindException e) {
        HashMap<String, String> results = new HashMap<>();
        List<ObjectError> errors = e.getAllErrors();
        for (ObjectError error : errors) {
            results.put(((FieldError) error).getField(), error.getDefaultMessage());
        }
        return TripleApiResponse.fail(results, ErrorCode.INVALID_PARAMETER);
    }

    // 타입이 맞지 않음, enum 변환 실패
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public TripleApiResponse<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return TripleApiResponse.fail(e.getMessage(), ErrorCode.INVALID_PARAMETER);
    }

    // path variable, requestParam 변환 실패
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public TripleApiResponse<String> handleMissingServletRequestParameterException(MethodArgumentTypeMismatchException e) {
        return TripleApiResponse.fail(e.getMessage(), ErrorCode.INVALID_PARAMETER);
    }
}
