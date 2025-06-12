package com.motorny.service.impl;

import com.motorny.dto.OrderDto;
import com.motorny.dto.admin.AdminOrderDto;
import com.motorny.dto.courier.CourierOrderDto;
import com.motorny.exceptions.OrderNotFoundException;
import com.motorny.mappers.OrderMapper;
import com.motorny.models.Order;
import com.motorny.models.User;
import com.motorny.models.enums.OrderStatus;
import com.motorny.repositories.OrderRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public OrderDto findOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderDto)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id '" + id + "'"));
    }

    @Override
    public OrderDto getOrderForEditing(Long id, UserDetails userDetails) {
        Order order = orderRepository.findByIdAndCustomerEmail(id, userDetails.getUsername())
                .orElse(null);

        if (order != null && order.getStatus() == OrderStatus.CREATED) {
            return orderMapper.toOrderDto(order);
        }
        return null;
    }

    @Override
    public boolean updateOrder(OrderDto orderDto, UserDetails userDetails) {
        Optional<Order> optional =
                orderRepository.findByIdAndCustomerEmail(orderDto.getId(), userDetails.getUsername());
        if (optional.isPresent()) {
            Order order = optional.get();

            if (order.getStatus() != OrderStatus.CREATED) {
                return false;
            }

            order.setReceiverFullName(orderDto.getReceiverFullName());
            order.setReceiverAddress(orderDto.getReceiverAddress());
            order.setLatitude(orderDto.getLatitude());
            order.setLongitude(orderDto.getLongitude());
            order.setReceiverPhone(orderDto.getReceiverPhone());
            order.setDescription(orderDto.getDescription());
            order.setEstimatedDelivery(orderDto.getEstimatedDelivery());
            order.setPaymentMethod(orderDto.getPaymentMethod());
            order.setOrderType(orderDto.getOrderType());
            order.setWeight(orderDto.getWeight());

            orderRepository.save(order);
            return true;
        }
        return false;
    }
}
