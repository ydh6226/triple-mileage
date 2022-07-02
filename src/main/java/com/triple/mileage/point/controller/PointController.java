package com.triple.mileage.point.controller;

import com.triple.mileage.common.api.TripleApiResponse;
import com.triple.mileage.point.controller.dto.UserAllPointEventsResponse;
import com.triple.mileage.point.controller.dto.UserPointResponse;
import com.triple.mileage.point.service.PointQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PointController {

    private final PointQueryService pointQueryService;

    @GetMapping("/point/{userId}")
    public TripleApiResponse<UserPointResponse> findUserPoint(@PathVariable UUID userId) {
        log.info("유저 누적 포인트 조회 userId: " + userId);

        UserPointResponse response = pointQueryService.findUserPoint(userId);
        return TripleApiResponse.ok(response);
    }

    @GetMapping("/point/{userId}/events")
    public TripleApiResponse<UserAllPointEventsResponse> findAllUserPointEvents(@PathVariable UUID userId) {
        log.info("유저 포인트 내역 조회 userId: " + userId);

        UserAllPointEventsResponse response = pointQueryService.findAllUserPointEvents(userId);
        return TripleApiResponse.ok(response);
    }

}
