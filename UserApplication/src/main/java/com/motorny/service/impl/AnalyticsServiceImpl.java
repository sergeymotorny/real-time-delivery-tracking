package com.motorny.service.impl;

import com.motorny.dto.admin.AnalyticsDto;
import com.motorny.models.Courier;
import com.motorny.models.Order;
import com.motorny.models.Shipment;
import com.motorny.models.enums.DeliveryType;
import com.motorny.models.enums.OrderStatus;
import com.motorny.repositories.CourierRepository;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.service.AnalyticsService;
import com.motorny.service.PriorityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final OrderRepository orderRepository;
    private final ShipmentRepository shipmentRepository;
    private final CourierRepository courierRepository;
    private final PriorityService priorityService;

    @Override
    public AnalyticsDto getAnalytics() {
        List<Order> allOrders = orderRepository.findAll();
        List<Shipment> allShipments = shipmentRepository.findAll();
        List<Courier> allCouriers = courierRepository.findAll();

        return AnalyticsDto.builder()
                .totalOrders(allOrders.size())
                .confirmedOrders(orderRepository.countByStatus(OrderStatus.CONFIRMED))
                .pendingOrders(orderRepository.countByStatus(OrderStatus.CREATED))
                .avgDeliveryHours(calculateAvgDeliveryHours(allOrders, allShipments))
                .courierWorkload(buildCourierWorkload(allCouriers))
                .ordersByPriority(buildOrdersByPriority(allOrders))
                .ordersByDeliveryType(buildOrdersByDeliveryType(allOrders))
                .ordersPerDay(buildOrdersPerDay())
                .topCourierNames(buildTopCourierNames(allCouriers))
                .topCourierCounts(buildTopCourierCounts(allCouriers))
                .build();
    }

    // --- private helpers ---

    /**
     * Average hours from order creation to shipment creation.
     * Only considers orders that have an associated shipment.
     */
    private double calculateAvgDeliveryHours(List<Order> orders, List<Shipment> shipments) {
        Map<Long, Shipment> shipmentByOrderId = shipments.stream()
                .filter(s -> s.getOrder() != null)
                .collect(Collectors.toMap(s -> s.getOrder().getId(), s -> s, (a, b) -> a));

        return orders.stream()
                .filter(o -> shipmentByOrderId.containsKey(o.getId())
                        && o.getCreatedAt() != null
                        && shipmentByOrderId.get(o.getId()).getCreatedAt() != null)
                .mapToLong(o -> {
                    LocalDateTime orderTime = o.getCreatedAt();
                    LocalDateTime shipmentTime = shipmentByOrderId.get(o.getId()).getCreatedAt();
                    return ChronoUnit.HOURS.between(orderTime, shipmentTime);
                })
                .average()
                .orElse(0.0);
    }

    /**
     * Number of active shipments per courier.
     */
    private Map<String, Long> buildCourierWorkload(List<Courier> couriers) {
        Map<String, Long> result = new LinkedHashMap<>();
        for (Courier courier : couriers) {
            String name = courier.getUser().getFirstName() + " " + courier.getUser().getLastName();
            long count = shipmentRepository.countByCourier(courier);
            result.put(name, count);
        }
        return result;
    }

    /**
     * Order count grouped by priority level (HIGH / MEDIUM / LOW).
     * Only CREATED orders have a meaningful priority.
     */
    private Map<String, Long> buildOrdersByPriority(List<Order> orders) {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("HIGH", 0L);
        result.put("MEDIUM", 0L);
        result.put("LOW", 0L);

        orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CREATED)
                .forEach(o -> {
                    String level = priorityService.getPriorityLevel(o).name();
                    result.merge(level, 1L, Long::sum);
                });

        return result;
    }

    /**
     * Order count split by delivery type (EXPRESS / STANDARD).
     */
    private Map<String, Long> buildOrdersByDeliveryType(List<Order> orders) {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("EXPRESS", orders.stream()
                .filter(o -> o.getDeliveryType() == DeliveryType.EXPRESS).count());
        result.put("STANDARD", orders.stream()
                .filter(o -> o.getDeliveryType() == DeliveryType.STANDARD).count());
        return result;
    }

    /**
     * Orders created per day for the last 7 days.
     */
    private Map<String, Long> buildOrdersPerDay() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Long> result = new LinkedHashMap<>();

        LocalDateTime now = LocalDateTime.now();
        for (int i = 6; i >= 0; i--) {
            LocalDateTime dayStart = now.minusDays(i).toLocalDate().atStartOfDay();
            LocalDateTime dayEnd = dayStart.plusDays(1);
            List<Order> dayOrders = orderRepository.findByCreatedAtBetween(dayStart, dayEnd);
            result.put(dayStart.format(fmt), (long) dayOrders.size());
        }

        return result;
    }

    /**
     * Top couriers sorted by total shipment count — names list.
     */
    private List<String> buildTopCourierNames(List<Courier> couriers) {
        return couriers.stream()
                .sorted(Comparator.comparingLong(
                        c -> -shipmentRepository.countByCourier(c)))
                .map(c -> c.getUser().getFirstName() + " " + c.getUser().getLastName())
                .collect(Collectors.toList());
    }

    /**
     * Top couriers sorted by total shipment count — counts list.
     */
    private List<Long> buildTopCourierCounts(List<Courier> couriers) {
        return couriers.stream()
                .sorted(Comparator.comparingLong(
                        c -> -shipmentRepository.countByCourier(c)))
                .map(c -> shipmentRepository.countByCourier(c))
                .collect(Collectors.toList());
    }
}
