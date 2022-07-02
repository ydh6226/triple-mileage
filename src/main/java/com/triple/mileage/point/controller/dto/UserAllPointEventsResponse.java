package com.triple.mileage.point.controller.dto;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class UserAllPointEventsResponse {

    private final int count;

    private final List<UserPointEventResponse> pointEvents;

    public static UserAllPointEventsResponse from(List<PointEvent> events) {
        return new UserAllPointEventsResponse(events.size(), toResponse(events));
    }

    private static List<UserPointEventResponse> toResponse(List<PointEvent> events) {
        return events.stream()
                .map(UserPointEventResponse::from)
                .collect(Collectors.toList());
    }

    @Getter
    @RequiredArgsConstructor
    private static class UserPointEventResponse {

        private final UUID id;

        private final UUID reviewId;

        private final Reason reason;

        private final UUID placeId;

        private final int mileage;

        private final LocalDateTime createdTime;

        public static UserPointEventResponse from(PointEvent event) {
            return new UserPointEventResponse(
                    event.getId(),
                    event.getReviewId(),
                    event.getReason(),
                    event.getPlaceId(),
                    event.getValue(),
                    event.getCreatedDate()
            );
        }
    }
}

