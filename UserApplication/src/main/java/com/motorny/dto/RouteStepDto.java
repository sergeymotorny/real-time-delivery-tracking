package com.motorny.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStepDto {

    private int step;                 // order in the route (1, 2, 3...)
    private Long orderId;
    private Long shipmentId;
    private String receiverFullName;
    private String receiverAddress;
    private Double latitude;
    private Double longitude;
    private double distanceFromPrevKm; // distance from previous stop
    private double totalDistanceKm;    // cumulative distance from depot
}
