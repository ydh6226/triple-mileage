package com.triple.mileage.point.service;

import com.triple.mileage.common.exception.ErrorCode;
import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.PointEvents;
import com.triple.mileage.point.domain.PointException;
import com.triple.mileage.point.repository.PointEventRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.rule.PointAdditionRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointEventService {

    private final List<PointAdditionRule> additionRules;

    private final PointEventRepository pointEventRepository;

    // TODO: 사용자 관점에서 첫 리뷰 테스트 추가
    /**
     * 추가된 포인트
     */
    @Transactional
    public int add(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");
        checkNoActiveReview(command.getPlaceId(), command.getUserId());

        List<PointEvent> events = additionRules.stream()
                .filter(rule -> rule.isCreatable(command))
                .map(rule -> rule.create(command))
                .collect(Collectors.toList());

        // TODO: batch insert로 변경
        pointEventRepository.saveAll(events);
        return sumPoints(events);
    }

    private void checkNoActiveReview(UUID placeId, UUID userId) {
        PointEvents events = PointEvents.activeEvents(pointEventRepository.findByPlaceIdAndUserIdOrderByCreatedDate(placeId, userId));
        if (events.hasEvents()) {
            String message = "해당 장소에 이미 작성된 리뷰가 있습니다. placeId: {0}, userId: {1}, events: {2}";
            throw new PointException(ErrorCode.ALREADY_WRITTEN_REVIEW, MessageFormat.format(message, placeId, userId, events));
        }
    }

    private int sumPoints(List<PointEvent> events) {
        return events.stream()
                .mapToInt(PointEvent::getValue)
                .sum();
    }

    /**
     * 회수된 포인트(음수 값)
     */
    @Transactional
    public int withdraw(UUID reviewId) {
        Assert.notNull(reviewId, "reviewId is required");
        return 0;
    }
}
