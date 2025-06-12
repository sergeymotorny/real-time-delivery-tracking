package com.motorny.service.impl;

import com.motorny.dto.ShipmentDto;
import com.motorny.exceptions.CourierNotFoundException;
import com.motorny.exceptions.OrderAlreadyTakenException;
import com.motorny.exceptions.OrderNotFoundException;
import com.motorny.exceptions.ShipmentNotFoundException;
import com.motorny.mappers.ShipmentMapper;
import com.motorny.models.Courier;
import com.motorny.models.Order;
import com.motorny.models.Shipment;
import com.motorny.models.User;
import com.motorny.models.enums.OrderStatus;
import com.motorny.repositories.CourierRepository;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.ShipmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.motorny.models.enums.OrderStatus.CONFIRMED;
import static com.motorny.models.enums.ShipmentStatus.IN_TRANSIT;

@Slf4j
@AllArgsConstructor
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final UserRepository userRepository;

    private final ShipmentMapper shipmentMapper;

    @Transactional
    @Override
    public ShipmentDto createShipmentForOrder(ShipmentDto shipmentDto, Long orderId, UserDetails userDetails) {

        String userByEmail = userDetails.getUsername();

        User foundUser = userRepository.findByEmail(userByEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userByEmail));
        Courier courier = courierRepository.findByUser(foundUser)
                .orElseThrow(() -> new CourierNotFoundException("Courier not found with id: " + foundUser.getId()));

        Shipment shipment = shipmentMapper.toShipment(shipmentDto);
        shipment.setCourier(courier);
        shipment.setCourierLatitude(46.974429);  //the starting point of the courier
        shipment.setCourierLongitude(32.019642); //the starting point of the courier

        Order foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        if (foundOrder.getStatus() != OrderStatus.CREATED) {
            throw new OrderAlreadyTakenException("Order already taken! Take another order!");
        }
        foundOrder.setStatus(CONFIRMED);

        shipment.setOrder(foundOrder);
        shipment.setStatus(IN_TRANSIT);
        Shipment savedShipment = shipmentRepository.save(shipment);

        return shipmentMapper.toShipmentDto(savedShipment);
    }

    @Override
    public List<ShipmentDto> getAllShipments() {
        return shipmentRepository.findAll().stream()
                .map(shipmentMapper::toShipmentDto)
                .toList();
    }

    @Override
    public ShipmentDto getById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found: " + id));
        return shipmentMapper.toShipmentDto(shipment);
    }

    @Override
    public ShipmentDto findByOrderId(Long orderId) {
        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found: " + orderId));
        return shipmentMapper.toShipmentDto(shipment);
    }
}