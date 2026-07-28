package com.motorny.service.impl;

import com.motorny.dto.ShipmentDto;
import com.motorny.exceptions.CourierNotFoundException;
import com.motorny.exceptions.OrderNotFoundException;
import com.motorny.mappers.ShipmentMapper;
import com.motorny.models.Courier;
import com.motorny.models.Order;
import com.motorny.models.Shipment;
import com.motorny.models.enums.OrderStatus;
import com.motorny.models.enums.ShipmentStatus;
import com.motorny.repositories.CourierRepository;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.service.AssignmentService;
import com.motorny.service.PriorityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class AssignmentServiceImpl implements AssignmentService {

    // Weight coefficients for the cost function
    // cost(courier, order) = α · distance + β · workload + γ · (1/priority)
    private static final double ALPHA = 0.5;  // distance weight
    private static final double BETA  = 0.3;  // workload weight
    private static final double GAMMA = 0.2;  // priority weight

    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentMapper shipmentMapper;
    private final PriorityService priorityService;

    @Transactional
    @Override
    public ShipmentDto assignCourierToOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Order is already assigned or completed: " + orderId);
        }

        List<Courier> availableCouriers = courierRepository.findAll();
        if (availableCouriers.isEmpty()) {
            throw new CourierNotFoundException("No couriers available for assignment");
        }

        // Select the courier with minimum cost
        Courier bestCourier = availableCouriers.stream()
                .min(Comparator.comparingDouble(courier -> calculateCost(courier, order)))
                .orElseThrow(() -> new CourierNotFoundException("Could not select a courier"));

        double cost = calculateCost(bestCourier, order);
        log.info("AssignmentService: order={}, assigned courier={}, cost={}",
                orderId, bestCourier.getId(), cost);

        // Create shipment with the assigned courier
        Shipment shipment = Shipment.builder()
                .order(order)
                .courier(bestCourier)
                .courierLatitude(46.974429)   // courier starting point (Mykolaiv depot)
                .courierLongitude(32.019642)
                .status(ShipmentStatus.IN_TRANSIT)
                .build();

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        Shipment saved = shipmentRepository.save(shipment);
        return shipmentMapper.toShipmentDto(saved);
    }

    /**
     * Cost function for courier-order assignment.
     * cost = α · normalizedDistance
     *      + β · workload
     *      + γ · (1 / priority)
     *
     * - distance: Haversine distance from courier's last known location to order pickup
     * - workload: number of active shipments the courier currently has
     * - priority: order weight in kg (heavier = higher priority = lower cost)
     */
    private double calculateCost(Courier courier, Order order) {
        double distance = calculateDistance(courier, order);
        double workload = getWorkload(courier);
        double priority = getPriority(order);

        double cost = ALPHA * distance + BETA * workload + GAMMA * (1.0 / priority);

        log.debug("Courier {}: distance={}, workload={}, priority={}, cost={}",
                courier.getId(), distance, workload, priority, cost);

        return cost;
    }

    /**
     * Haversine formula — great-circle distance in km between
     * courier's last known position and the order's destination.
     * Falls back to 0 if no location data is available.
     */
    private double calculateDistance(Courier courier, Order order) {
        if (order.getLatitude() == null || order.getLongitude() == null) {
            return 0.0;
        }

        // Use courier's last recorded location if available, otherwise use depot
        double courierLat = 46.974429;
        double courierLng = 32.019642;

        if (!courier.getLocations().isEmpty()) {
            var lastLocation = courier.getLocations()
                    .get(courier.getLocations().size() - 1);
            courierLat = lastLocation.getLatitude();
            courierLng = lastLocation.getLongitude();
        }

        return haversine(courierLat, courierLng,
                order.getLatitude(), order.getLongitude());
    }

    /**
     * Returns the number of active (IN_TRANSIT) shipments for the courier.
     * Used as the workload metric.
     */
    private double getWorkload(Courier courier) {
        return shipmentRepository.countByCourierAndStatus(
                courier, ShipmentStatus.IN_TRANSIT);
    }

    /**
     * Priority based on full PriorityService score.
     * Minimum value is 0.1 to avoid division by zero.
     */
    private double getPriority(Order order) {
        double score = priorityService.calculateScore(order);
        return Math.max(score, 0.1);
    }

    /**
     * Haversine formula to calculate distance in km between two coordinates.
     */
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return EARTH_RADIUS_KM * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
