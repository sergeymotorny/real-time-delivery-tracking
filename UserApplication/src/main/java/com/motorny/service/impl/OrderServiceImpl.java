package com.motorny.service.impl;

import com.motorny.dto.OrderDto;
import com.motorny.dto.admin.AdminOrderDto;
import com.motorny.dto.courier.CourierOrderDto;
import com.motorny.exceptions.OrderNotFoundException;
import com.motorny.mappers.OrderMapper;
import com.motorny.models.Order;
import com.motorny.models.User;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final OrderMapper orderMapper;


    @Override
    public List<AdminOrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toAdminOrderDto)
                .toList();
    }

    @Override
    public List<OrderDto> getOrdersByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));

        return orderRepository.findByCustomer(user).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public CourierOrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toCourierOrderDto)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id '" + id + "'"));
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto, UserDetails userDetails) {

        String userByEmail = userDetails.getUsername();

        User foundUser = userRepository.findByEmail(userByEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userByEmail));

        Order order = orderMapper.toOrder(orderDto);
        order.setCustomer(foundUser);

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderDto(savedOrder);
    }
}
