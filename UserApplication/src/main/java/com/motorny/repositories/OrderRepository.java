package com.motorny.repositories;

import com.motorny.models.Order;
import com.motorny.models.User;
import com.motorny.models.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User user);
    Optional<Order> findByIdAndCustomerEmail(Long id, String email);
    List<Order> findByStatus(OrderStatus status);
    long countByStatus(OrderStatus status);
    List<Order> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
