package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.PointEvents;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.dto.PointModificationCommand;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

@Component
public class PhotoPointRule implements PointAdditionRule, PointModificationRule {

    private static final Reason DEFAULT_REASON = Reason.ATTACH_PHOTO;

    // PointAdditionRule
    @Override
    public boolean isCreatable(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return !ObjectUtils.isEmpty(command.getPhotoIds());
    }

    @Override
    public PointEvent create(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return command.toEntity(DEFAULT_REASON);
    }

    // PointModificationRule
    /**
     * 수정 전 사진 존재 o, 수정 후 사진 존재 o => 포인트 변경 없음. <br>
     * 수정 전 사진 존재 x, 수정 후 사진 존재 x => 포인트 변경 없음. <br>
     * 수정 전 사진 존재 o, 수정 후 사진 존재 x => 포인트 변경 필요함. <br>
     * 수정 전 사진 존재 x, 수정 후 사진 존재 o => 포인트 변경 필요함. <br>
     * 즉, PointEvents.hasReason() 과 ObjectUtils.isEmpty() 결과가 같으면 수정이 필요함.
     */
    @Override
    public boolean isModificationRequired(PointEvents pointEvents, PointModificationCommand command) {
        return pointEvents.hasReason(DEFAULT_REASON) == ObjectUtils.isEmpty(command.getPhotoIds());
    }

    @Override
    public PointEvent modify(PointEvents pointEvents) {
        return pointEvents.modify(DEFAULT_REASON);
    }
}
