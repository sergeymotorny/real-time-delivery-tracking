package com.motorny.service.impl;

import com.motorny.exceptions.ShipmentNotFoundException;
import com.motorny.models.Shipment;
import com.motorny.models.User;
import com.motorny.models.enums.ShipmentStatus;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public void createShipment(Shipment shipment, Principal principal) {
        String userEmail = principal.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        shipment.setStatus(ShipmentStatus.CREATED);
        shipment.setCustomer(user);
        shipmentRepository.save(shipment);
    }

    @Override
    public Shipment getById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment not found exception"));
    }
}
