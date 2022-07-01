package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Component
public class ContentPointRule implements PointAdditionRule {

    @Override
    public boolean isCreatable(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return StringUtils.hasText(command.getContent());
    }

    @Override
    public PointEvent create(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return command.toEntity(Reason.ADD_CONTENT);
    }
}
