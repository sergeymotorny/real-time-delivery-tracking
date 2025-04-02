package com.motorny.repositories;

import com.motorny.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByShipmentId(@Param("shipmentId") Long shipmentId);

    @Query(value = "SELECT f FROM Feedback f WHERE f.user.id = :userId")
    List<Feedback> getFeedbackByCourierId(@Param("userId") Long userId);
}
