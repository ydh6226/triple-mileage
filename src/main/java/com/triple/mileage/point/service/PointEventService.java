package com.triple.mileage.point.service;

import com.triple.mileage.common.exception.ErrorCode;
import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.PointEvents;
import com.triple.mileage.point.domain.PointException;
import com.triple.mileage.point.repository.PointEventRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.dto.PointModificationCommand;
import com.triple.mileage.point.service.rule.PointAdditionRule;
import com.triple.mileage.point.service.rule.PointModificationRule;
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
    private final List<PointModificationRule> modificationRules;

    private final PointEventRepository pointEventRepository;

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

    @Transactional
    public int modify(PointModificationCommand command) {
        Assert.notNull(command, "PointModificationCommand is required");

        PointEvents pointEvents = findActivePointEvents(command.getReviewId());
        if (pointEvents.isEmpty()) {
            String message = "수정할 수 있는 유요한 포인트 내역이 없습니다. userId: {0}, reviewId:{1}";
            throw new PointException(ErrorCode.NO_VALID_POINT_EVENTS, MessageFormat.format(message, command.getUserId(), command.getReviewId()));
        }

        List<PointEvent> modifiedEvents = modificationRules.stream()
                .filter(rule -> rule.isModificationRequired(pointEvents, command))
                .map(rule -> rule.modify(pointEvents))
                .collect(Collectors.toList());

        pointEventRepository.saveAll(modifiedEvents);
        return sumPoints(modifiedEvents);
    }

    /**
     * 회수된 포인트(음수 값)
     */
    @Transactional
    public int withdraw(UUID reviewId) {
        Assert.notNull(reviewId, "reviewId is required");

        PointEvents pointEvents = findActivePointEvents(reviewId);
        List<PointEvent> withdrawEvents = pointEvents.compensate();

        pointEventRepository.saveAll(withdrawEvents);
        return sumPoints(withdrawEvents);
    }

    private PointEvents findActivePointEvents(UUID reviewId) {
        return PointEvents.activeEvents(pointEventRepository.findByReviewIdOrderByCreatedDate(reviewId));
    }
}
