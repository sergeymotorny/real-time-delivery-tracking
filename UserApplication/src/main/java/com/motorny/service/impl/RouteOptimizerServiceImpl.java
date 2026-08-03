package com.motorny.service.impl;

import com.motorny.dto.RouteStepDto;
import com.motorny.exceptions.CourierNotFoundException;
import com.motorny.models.Courier;
import com.motorny.models.Order;
import com.motorny.models.Shipment;
import com.motorny.models.User;
import com.motorny.repositories.CourierRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.RouteOptimizerService;
import com.motorny.util.GeoUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class RouteOptimizerServiceImpl implements RouteOptimizerService {

    // Mykolaiv depot — courier starting point
    private static final double DEPOT_LAT = 46.974429;
    private static final double DEPOT_LNG = 32.019642;

    private final UserRepository userRepository;
    private final CourierRepository courierRepository;
    private final ShipmentRepository shipmentRepository;

    /**
     * Nearest Neighbor algorithm:
     *
     * 1. Start at depot
     * 2. Find the closest unvisited order
     * 3. Move to it, mark as visited
     * 4. Repeat until all orders are visited
     *
     * Time complexity: O(n²) — acceptable for realistic courier workloads (n ≤ 20)
     */
    @Override
    public List<RouteStepDto> buildOptimizedRoute(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));

        Courier courier = courierRepository.findByUser(user)
                .orElseThrow(() -> new CourierNotFoundException("Courier not found for user: " + user.getId()));

        List<Shipment> shipments = shipmentRepository.findByCourier(courier);

        // Filter only shipments that have valid coordinates
        List<Shipment> routable = shipments.stream()
                .filter(s -> s.getOrder() != null
                        && s.getOrder().getLatitude() != null
                        && s.getOrder().getLongitude() != null)
                .toList();

        if (routable.isEmpty()) {
            log.info("No routable shipments for courier {}", courier.getId());
            return List.of();
        }

        return nearestNeighbor(routable);
    }

    private List<RouteStepDto> nearestNeighbor(List<Shipment> shipments) {
        List<Shipment> unvisited = new ArrayList<>(shipments);
        List<RouteStepDto> route = new ArrayList<>();

        double currentLat = DEPOT_LAT;
        double currentLng = DEPOT_LNG;
        double totalDistance = 0.0;
        int step = 1;

        while (!unvisited.isEmpty()) {
            // Find the closest unvisited shipment
            Shipment nearest = null;
            double minDist = Double.MAX_VALUE;

            for (Shipment s : unvisited) {
                double dist = GeoUtils.haversine(
                        currentLat, currentLng,
                        s.getOrder().getLatitude(),
                        s.getOrder().getLongitude());
                if (dist < minDist) {
                    minDist = dist;
                    nearest = s;
                }
            }

            if (nearest == null) break;

            totalDistance += minDist;
            Order order = nearest.getOrder();

            route.add(RouteStepDto.builder()
                    .step(step++)
                    .orderId(order.getId())
                    .shipmentId(nearest.getId())
                    .receiverFullName(order.getReceiverFullName())
                    .receiverAddress(order.getReceiverAddress())
                    .latitude(order.getLatitude())
                    .longitude(order.getLongitude())
                    .distanceFromPrevKm(Math.round(minDist * 100.0) / 100.0)
                    .totalDistanceKm(Math.round(totalDistance * 100.0) / 100.0)
                    .build());

            currentLat = order.getLatitude();
            currentLng = order.getLongitude();
            unvisited.remove(nearest);

            log.debug("Route step {}: order={}, dist={}km, total={}km",
                    step - 1, order.getId(), minDist, totalDistance);
        }

        log.info("Route built for {} stops, total distance: {}km",
                route.size(), totalDistance);

        return route;
    }
}
