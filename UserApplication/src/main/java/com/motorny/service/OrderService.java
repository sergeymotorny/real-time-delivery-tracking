package com.motorny.service;

import com.motorny.dto.OrderDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    List<OrderDto> getOrdersByUser(String email);
    OrderDto createOrder(OrderDto orderDto, UserDetails userDetails);
}
