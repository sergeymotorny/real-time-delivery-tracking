package com.motorny.dto;

import com.motorny.models.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDto {

    private Long id;
    private Long trackingNumber;
    private Double courierLatitude;
    private Double courierLongitude;
    private ShipmentStatus status;
    private LocalDateTime createdAt;

    // Order info for courier's "my shipments" view
    private Long orderId;
    private String receiverFullName;
    private String receiverAddress;
    private String receiverPhone;
}
