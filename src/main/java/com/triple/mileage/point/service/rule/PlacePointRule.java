package com.triple.mileage.point.service.rule;

import com.triple.mileage.point.domain.PointEvent;
import com.triple.mileage.point.domain.Reason;
import com.triple.mileage.point.repository.PointEventQueryRepository;
import com.triple.mileage.point.service.dto.PointAdditionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class PlacePointRule implements PointAdditionRule {

    private final PointEventQueryRepository pointEventQueryRepository;

    @Override
    public boolean isCreatable(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return !pointEventQueryRepository.existsActiveReviewAt(command.getPlaceId());
    }

    @Override
    public PointEvent create(PointAdditionCommand command) {
        Assert.notNull(command, "PointAdditionCommand is required");

        return command.toEntity(Reason.ADD_FIRST_REVIEW);
    }
}
