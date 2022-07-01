package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@Component
public class PhotoPointRule implements PointAdditionRule {

    @Override
    public boolean isCreatable(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return !ObjectUtils.isEmpty(command.getPhotoIds());
    }

    @Override
    public PointEvent create(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return command.toEntity(Reason.ATTACH_PHOTO);
    }
}
