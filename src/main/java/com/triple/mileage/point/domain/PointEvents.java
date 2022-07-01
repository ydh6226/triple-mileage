package com.triple.mileage.point.domain;

import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

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
}
