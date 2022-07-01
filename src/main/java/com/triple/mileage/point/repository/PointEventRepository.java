package com.triple.mileage.point.repository;

import com.triple.mileage.point.domain.PointEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PointEventRepository extends JpaRepository<PointEvent, UUID> {

    List<PointEvent> findByPlaceIdAndUserIdOrderByCreatedDate(UUID placeId, UUID userId);

    List<PointEvent> findByReviewId(UUID reviewId);
}
