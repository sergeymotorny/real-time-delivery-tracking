package com.motorny.repositories;

import com.motorny.models.Courier;
import com.motorny.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    Optional<Courier> findByUser(User user);
}
