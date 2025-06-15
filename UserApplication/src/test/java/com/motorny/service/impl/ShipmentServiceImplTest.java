package com.motorny.service.impl;

import com.motorny.dto.ShipmentDto;
import com.motorny.mappers.ShipmentMapper;
import com.motorny.models.Courier;
import com.motorny.models.Order;
import com.motorny.models.Shipment;
import com.motorny.models.User;
import com.motorny.models.enums.OrderStatus;
import com.motorny.models.enums.ShipmentStatus;
import com.motorny.repositories.CourierRepository;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceImplTest {

    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Mock private ShipmentRepository shipmentRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private CourierRepository courierRepository;
    @Mock private ShipmentMapper shipmentMapper;

    private User user;
    private Courier courier;
    private Order order;
    private Shipment shipment;
    private ShipmentDto shipmentDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("courier@gmail.com");

        courier = new Courier();
        courier.setId(1L);
        courier.setUser(user);

        order = new Order();
        order.setId(100L);
        order.setStatus(OrderStatus.CREATED);

        shipment = new Shipment();
        shipment.setId(10L);
        shipment.setOrder(order);
        shipment.setCourier(courier);
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);

        shipmentDto = new ShipmentDto();
    }

    @Test
    void test_createShipmentForOrder_Success() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("courier@gmail.com")
                .password("mypass11@").roles("COURIER").build();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(courierRepository.findByUser(user)).thenReturn(Optional.of(courier));
        Mockito.when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        Mockito.when(shipmentMapper.toShipment(shipmentDto)).thenReturn(shipment);
        Mockito.when(shipmentRepository.save(any())).thenReturn(shipment);
        Mockito.when(shipmentMapper.toShipmentDto(shipment)).thenReturn(shipmentDto);

        ShipmentDto result = shipmentService.createShipmentForOrder(shipmentDto, order.getId(), userDetails);

        assertNotNull(result);
        assertEquals(ShipmentStatus.IN_TRANSIT, shipment.getStatus());
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        Mockito.verify(shipmentRepository).save(any());
    }
}