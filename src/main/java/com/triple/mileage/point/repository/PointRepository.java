package com.triple.mileage.point.repository;

import com.triple.mileage.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PointRepository extends JpaRepository<Point, UUID> {
}
