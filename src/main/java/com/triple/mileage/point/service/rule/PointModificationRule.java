package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.PointEvents;
import com.triple.mileage.point.service.dto.PointModificationCommand;

public interface PointModificationRule {

    boolean isModificationRequired(PointEvents pointEvents, PointModificationCommand command);

    PointEvent modify(PointEvents pointEvents);
}
