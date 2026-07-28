package com.motorny.service;

import com.motorny.dto.admin.AdminOrderDto;
import com.motorny.models.Order;
import com.motorny.models.enums.PriorityLevel;

import java.util.List;

public interface PriorityService {

    /**
     * Calculates a numeric priority score for an order.
     * Higher score = higher priority.
     *
     * score = w1·(1/time_left_hours) + w2·weight + w3·delivery_type_coefficient
     */
    double calculateScore(Order order);

    /**
     * Returns a human-readable priority level based on the score.
     */
    PriorityLevel getPriorityLevel(Order order);

    /**
     * Returns all CREATED orders sorted by priority score descending.
     */
    List<AdminOrderDto> getOrdersSortedByPriority();
}
