package com.motorny.service;

import com.motorny.dto.OrderDto;
import com.motorny.dto.admin.AdminOrderDto;
import com.motorny.dto.courier.CourierOrderDto;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {
    List<AdminOrderDto> getAllOrders();
    List<OrderDto> getOrdersByUser(String email);
    CourierOrderDto getOrderById(Long id);
    OrderDto createOrder(OrderDto orderDto, UserDetails userDetails);
    OrderDto findOrderById(Long id);
    OrderDto getOrderForEditing(Long id, UserDetails userDetails);
    boolean updateOrder(OrderDto orderDto, UserDetails userDetails);
}
