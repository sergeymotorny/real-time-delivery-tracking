package com.motorny.repositories;

import com.motorny.models.Courier;
import com.motorny.models.Shipment;
import com.motorny.models.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrderId(Long orderId);
    long countByCourierAndStatus(Courier courier, ShipmentStatus status);
}
