package com.triple.mileage.review;

import com.triple.mileage.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class ReviewEventHandler {

    private final PointService pointService;

    public void handle(ReviewEventRequest request) {
        Assert.notNull(request, "ReviewEventRequest is required");

        switch (request.getAction()) {
            case ADD:
                pointService.add(request.toAdditionCommand());
                break;
            case MOD:
                pointService.modifyReviewPoint(request.toModificationCommand());
                break;
            case DELETE:
                pointService.withdrawReviewPoint(request.getUserId(), request.getReviewId());
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 ACTION 입니다. action: " + request.getAction());
        }
    }
}
