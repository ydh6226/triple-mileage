package com.triple.mileage.point.service;

import com.triple.mileage.point.domain.Point;
import com.triple.mileage.point.repository.PointRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.dto.PointModificationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointEventService pointEventService;

    @Transactional
    public void add(PointAdditionCommand command) {
        Assert.notNull(command, "pointAdditionCommand is required");

        Point point = findOrCreate(command.getUserId());

        int additionalPoint = pointEventService.add(command);
        point.sum(additionalPoint);

        pointRepository.save(point);
    }

    private Point findOrCreate(UUID userId) {
        return pointRepository.findById(userId)
                .orElseGet(() -> new Point(userId));
    }

    @Transactional
    public void modifyReviewPoint(PointModificationCommand command) {
        Assert.notNull(command, "PointModificationCommand is required");

        Point point = findByUserId(command.getUserId());

        int changedPoint = pointEventService.modify(command);
        point.sum(changedPoint);
    }

    @Transactional
    public void withdrawReviewPoint(UUID userId, UUID reviewId) {
        Point point = findByUserId(userId);

        int changedPoint = pointEventService.withdraw(reviewId);
        point.sum(changedPoint);
    }

    private Point findByUserId(UUID userId) {
        Assert.notNull(userId, "userId is required");

        return pointRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("포인트 지급내역이 없어서 포인트를 회수할 수 없습니다. userId: " + userId));
    }
}
