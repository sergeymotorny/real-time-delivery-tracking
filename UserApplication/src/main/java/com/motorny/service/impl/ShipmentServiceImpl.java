package com.motorny.service.impl;

import com.motorny.models.Shipment;
import com.motorny.repositories.ShipmentRepository;
import com.motorny.service.ShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public void create(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    @Override
    public Shipment getById(Long id) {
        return null;
    }
}
