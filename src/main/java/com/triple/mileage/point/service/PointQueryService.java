package com.triple.mileage.point.service;

import com.triple.mileage.point.controller.dto.UserAllPointEventsResponse;
import com.triple.mileage.point.controller.dto.UserPointResponse;
import com.triple.mileage.point.domain.Point;
import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.repository.PointEventRepository;
import com.triple.mileage.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointQueryService {

    private final PointRepository pointRepository;
    private final PointEventRepository pointEventRepository;

    public UserPointResponse findUserPoint(UUID userId) {
        Assert.notNull(userId, "userid is required");

        Point point = pointRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저의 포인트 생성기록이 없습니다. userId: " + userId));

        return UserPointResponse.from(point);
    }

    public UserAllPointEventsResponse findAllUserPointEvents(UUID userId) {
        Assert.notNull(userId, "userid is required");

        List<PointEvent> events = pointEventRepository.findByUserIdOrderByCreatedDateDesc(userId);
        return UserAllPointEventsResponse.from(events);
    }
}
