package com.motorny.repositories;

import com.motorny.models.Order;
import com.motorny.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User user);
    Optional<Order> findByIdAndCustomerEmail(Long id, String email);
}
