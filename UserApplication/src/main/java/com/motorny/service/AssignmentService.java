package com.motorny.service;

import com.motorny.dto.ShipmentDto;

public interface AssignmentService {

    /**
     * Automatically assigns the optimal courier to the given order
     * based on a weighted cost function: distance + workload + priority.
     *
     * @param orderId the ID of the order to assign
     * @return ShipmentDto of the created shipment
     */
    ShipmentDto assignCourierToOrder(Long orderId);
}
