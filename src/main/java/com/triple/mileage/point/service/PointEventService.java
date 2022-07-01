package com.triple.mileage.point.service;

import com.triple.mileage.point.service.dto.PointAdditionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointEventService {
    public int add(PointAdditionCommand command) {
        return 0;
    }
}
