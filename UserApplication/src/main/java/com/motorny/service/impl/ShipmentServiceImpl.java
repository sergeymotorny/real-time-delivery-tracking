package com.motorny.service.impl;

import com.motorny.dto.ShipmentDto;
import com.motorny.exceptions.CourierNotFoundException;
import com.motorny.exceptions.OrderNotFoundException;
import com.motorny.exceptions.ShipmentNotFoundException;
import com.motorny.mappers.ShipmentMapper;
import com.motorny.models.Courier;
import com.motorny.models.Order;
import com.motorny.models.Shipment;
import com.motorny.repositories.CourierRepository;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.service.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.motorny.models.enums.OrderStatus.CONFIRMED;
import static com.motorny.models.enums.ShipmentStatus.IN_TRANSIT;

@AllArgsConstructor
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;

    private final ShipmentMapper shipmentMapper;

    @Override
    public List<ShipmentDto> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(shipmentMapper::toShipmentDto)
                .toList();
    }

    @Override
    public ShipmentDto createShipmentForOrder(ShipmentDto shipmentDto, Long orderId, UserDetails userDetails) {

        String userByEmail = userDetails.getUsername();
        Courier foundCourier = courierRepository.findByEmail(userByEmail)
                .orElseThrow(() -> new CourierNotFoundException("User not found with email: " + userByEmail));

        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        foundOrder.setStatus(CONFIRMED);

        Shipment shipment = shipmentMapper.toShipment(shipmentDto);
        shipment.setOrder(foundOrder);
        shipment.setStatus(IN_TRANSIT);
        shipment.setCourier(foundCourier);

        Shipment savedShipment = shipmentRepository.save(shipment);

        return shipmentMapper.toShipmentDto(savedShipment);
    }

    @Override
    public ShipmentDto getById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found: " + id));
        return shipmentMapper.toShipmentDto(shipment);
    }
}
