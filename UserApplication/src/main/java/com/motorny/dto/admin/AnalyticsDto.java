package com.motorny.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDto {

    // --- Summary cards ---
    private long totalOrders;
    private long confirmedOrders;
    private long pendingOrders;        // CREATED status
    private double avgDeliveryHours;   // average hours from order creation to shipment acceptance

    // --- Courier workload ---
    // key: "FirstName LastName", value: number of active shipments
    private Map<String, Long> courierWorkload;

    // --- Orders by priority ---
    // key: "HIGH" / "MEDIUM" / "LOW", value: count
    private Map<String, Long> ordersByPriority;

    // --- Orders by delivery type ---
    // key: "EXPRESS" / "STANDARD", value: count
    private Map<String, Long> ordersByDeliveryType;

    // --- Orders per day (last 7 days) ---
    // key: "2026-07-30", value: count
    private Map<String, Long> ordersPerDay;

    // --- Top couriers by completed shipments ---
    private List<String> topCourierNames;
    private List<Long> topCourierCounts;
}
