package com.triple.mileage.point.domain;

import com.triple.mileage.common.exception.ErrorCode;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@ToString
public class PointEvents {

    private final List<PointEvent> events;

    private PointEvents(List<PointEvent> events) {
        Assert.notNull(events, "PointEvents.events is required");

        this.events = events;
    }

    public static PointEvents activeEvents(List<PointEvent> events) {
        Assert.notNull(events, "PointEvents.events is required");

        Set<PointEvent> activeEvents = new HashSet<>();
        for (PointEvent event : events) {
            PointEvent compensation = event.compensate();
            if (activeEvents.contains(compensation)) {
                activeEvents.remove(compensation);
                continue;
            }
            activeEvents.add(event);
        }
        return new PointEvents(new ArrayList<>(activeEvents));
    }

    public boolean hasEvents() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(events);
    }

    public List<PointEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public List<PointEvent> compensate() {
        return events.stream()
                .map(PointEvent::compensate)
                .collect(Collectors.toList());
    }

    public boolean hasReason(Reason reason) {
        return events.stream()
                .anyMatch(event -> event.reasonEquals(reason));
    }

    /**
     * 입력으로 받은 Reason에 해당하는 이벤트가 있다면 해당 이벤트를 보상하는 이벤트를 반환. <br>
     * 없다면 Reason에 해당하는 이벤트를 생성.
     */
    public PointEvent modify(Reason reason) {
        if (ObjectUtils.isEmpty(events)) {
            throw new PointException(ErrorCode.NO_VALID_POINT_EVENTS, "유효한 포인트 내역이 없어서 포인트 수정을 할 수 없습니다.");
        }

        for (PointEvent event : events) {
            if (event.reasonEquals(reason)) {
                return event.compensate();
            }
        }

        return createEvent(reason);
    }

    private PointEvent createEvent(Reason reason) {
        PointEvent pointEvent = events.get(0);
        return pointEvent.generateChangedReason(reason);
    }
}
