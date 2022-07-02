package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.PointEvents;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import com.triple.mileage.point.service.dto.PointModificationCommand;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Component
public class ContentPointRule implements PointAdditionRule, PointModificationRule {

    private static final Reason DEFAULT_REASON = Reason.ADD_CONTENT;

    // PointAdditionRule
    @Override
    public boolean isCreatable(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return StringUtils.hasText(command.getContent());
    }

    @Override
    public PointEvent create(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return command.toEntity(DEFAULT_REASON);
    }

    // PointModificationRule
    /**
     * 수정 전 텍스트 존재 o, 수정 후 텍스트 존재 o => 포인트 변경 없음. <br>
     * 수정 전 텍스트 존재 x, 수정 후 텍스트 존재 x => 포인트 변경 없음. <br>
     * 수정 전 텍스트 존재 o, 수정 후 텍스트 존재 x => 포인트 변경 필요함. <br>
     * 수정 전 텍스트 존재 x, 수정 후 텍스트 존재 o => 포인트 변경 필요함. <br>
     * 즉, PointEvents.hasReason() 과 StringUtils.hasText 결과가 다르면 수정이 필요함.
     */
    @Override
    public boolean isModificationRequired(PointEvents pointEvents, PointModificationCommand command) {
        Assert.notNull(pointEvents, "PointEvents is required");
        Assert.notNull(command, "PointModificationCommand is required");

        return pointEvents.hasReason(DEFAULT_REASON) != StringUtils.hasText(command.getContent());
    }

    @Override
    public PointEvent modify(PointEvents pointEvents) {
        return pointEvents.modify(DEFAULT_REASON);
    }
}
