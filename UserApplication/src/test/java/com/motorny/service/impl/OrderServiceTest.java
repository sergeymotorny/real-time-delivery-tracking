package com.motorny.service.impl;

import com.motorny.dto.OrderDto;
import com.motorny.mappers.OrderMapper;
import com.motorny.models.Order;
import com.motorny.models.User;
import com.motorny.models.enums.DeliveryType;
import com.motorny.models.enums.OrderPaymentMethod;
import com.motorny.models.enums.OrderType;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock private OrderRepository orderRepository;

    @Mock private UserRepository userRepository;

    @Mock private OrderMapper orderMapper;

    private OrderDto orderDto;
    private Order order;
    private User mockUser;


    @BeforeEach
    public void setup() {
        mockUser = User.builder()
                .id(1L)
                .firstName("Ronald")
                .lastName("Kick")
                .phone("+380504003020")
                .email("r_kick@gmail.com")
                .password("ronaldKick")
                .build();

        orderDto = OrderDto.builder()
                .receiverFullName("Sergey Motorny")
                .receiverAddress("Naberezhnaya street 7")
                .latitude(46.950989)
                .longitude(32.039669)
                .receiverPhone("+380702003010")
                .description("Products")
                .estimatedDelivery(LocalDateTime.of(2025, 6, 10, 12, 30))
                .deliveryType(DeliveryType.STANDARD)
                .paymentMethod(OrderPaymentMethod.TERMINAL)
                .orderType(OrderType.PARCEL)
                .weight(11.3)
                .build();

        order = new Order();
        order.setId(1L);
        order.setCustomer(mockUser);
        order.setDescription(orderDto.getDescription());
    }

    @Test
    public void check_creationOfTheOrder() {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("r_kick@gmail.com")
                .password("userpass11")
                .roles("CLIENT")
                .build();

        Mockito.when(userRepository.findByEmail("r_kick@gmail.com"))
                .thenReturn(Optional.of(mockUser));
        Mockito.when(orderMapper.toOrder(orderDto)).thenReturn(order);
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        Mockito.when(orderMapper.toOrderDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.createOrder(orderDto, userDetails);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(orderDto.getDescription(), result.getDescription());
        Mockito.verify(orderRepository).save(order);
        Mockito.verify(orderMapper).toOrderDto(order);
    }
}