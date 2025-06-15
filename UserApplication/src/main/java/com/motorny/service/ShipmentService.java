package com.motorny.service;

import com.motorny.dto.ShipmentDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ShipmentService {
    List<ShipmentDto> getAllShipments();
    ShipmentDto createShipmentForOrder(ShipmentDto shipmentDto, Long orderId, UserDetails userDetails);
    ShipmentDto getById(Long id);
    ShipmentDto findByOrderId(Long orderId);
}
