package com.triple.mileage.point.service;

import com.triple.mileage.point.domain.Point;
import com.triple.mileage.point.repository.PointRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
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
}