package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.service.dto.PointAdditionCommand;

public interface PointAdditionRule {

    boolean isCreatable(PointAdditionCommand command);

    PointEvent create(PointAdditionCommand command);
}
