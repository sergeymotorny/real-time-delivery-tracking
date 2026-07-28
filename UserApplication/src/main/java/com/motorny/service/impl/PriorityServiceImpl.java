package com.motorny.service.impl;

import com.motorny.dto.admin.AdminOrderDto;
import com.motorny.mappers.OrderMapper;
import com.motorny.models.Order;
import com.motorny.models.enums.DeliveryType;
import com.motorny.models.enums.OrderStatus;
import com.motorny.models.enums.PriorityLevel;
import com.motorny.repositories.OrderRepository;
import com.motorny.service.PriorityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PriorityServiceImpl implements PriorityService {

    // Weight coefficients
    private static final double W1 = 0.5;  // urgency (time left)
    private static final double W2 = 0.3;  // weight of cargo
    private static final double W3 = 0.2;  // delivery type

    // Score thresholds for priority levels
    private static final double HIGH_THRESHOLD   = 1.5;
    private static final double MEDIUM_THRESHOLD = 0.5;

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    /**
     * Priority score formula:
     *
     * score = W1 · (1 / time_left_hours)
     *       + W2 · normalized_weight
     *       + W3 · delivery_type_coefficient
     *
     * time_left_hours: hours until estimatedDelivery (min 1h to avoid division by zero)
     * normalized_weight: weight / 100 (so 100kg = 1.0 unit)
     * delivery_type_coefficient: EXPRESS = 2.0, STANDARD = 1.0
     */
    @Override
    public double calculateScore(Order order) {
        double urgency = calculateUrgency(order);
        double weightScore = calculateWeightScore(order);
        double typeScore = calculateTypeScore(order);

        double score = W1 * urgency + W2 * weightScore + W3 * typeScore;

        log.debug("Order {}: urgency={}, weight={}, type={}, score={}",
                order.getId(), urgency, weightScore, typeScore, score);

        return score;
    }

    @Override
    public PriorityLevel getPriorityLevel(Order order) {
        double score = calculateScore(order);
        if (score >= HIGH_THRESHOLD) return PriorityLevel.HIGH;
        if (score >= MEDIUM_THRESHOLD) return PriorityLevel.MEDIUM;
        return PriorityLevel.LOW;
    }

    @Override
    public List<AdminOrderDto> getOrdersSortedByPriority() {
        return orderRepository.findByStatus(OrderStatus.CREATED).stream()
                .sorted(Comparator.comparingDouble(this::calculateScore).reversed())
                .map(order -> {
                    AdminOrderDto dto = orderMapper.toAdminOrderDto(order);
                    dto.setPriorityLevel(getPriorityLevel(order));
                    return dto;
                })
                .toList();
    }

    // --- private helpers ---

    /**
     * Urgency = 1 / hours_until_deadline
     * The closer the deadline, the higher the urgency.
     * Minimum 1 hour to prevent division by zero.
     */
    private double calculateUrgency(Order order) {
        if (order.getEstimatedDelivery() == null) return 0.0;
        long hoursLeft = ChronoUnit.HOURS.between(LocalDateTime.now(), order.getEstimatedDelivery());
        hoursLeft = Math.max(hoursLeft, 1); // floor at 1 hour
        return 1.0 / hoursLeft;
    }

    /**
     * Normalized weight: weight / 100
     * 100kg → 1.0, 50kg → 0.5, 10kg → 0.1
     */
    private double calculateWeightScore(Order order) {
        if (order.getWeight() == null || order.getWeight() <= 0) return 0.0;
        return order.getWeight() / 100.0;
    }

    /**
     * EXPRESS = 2.0 (higher urgency)
     * STANDARD = 1.0
     */
    private double calculateTypeScore(Order order) {
        if (order.getDeliveryType() == DeliveryType.EXPRESS) return 2.0;
        return 1.0;
    }
}
