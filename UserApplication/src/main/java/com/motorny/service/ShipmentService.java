package com.motorny.service;

import com.motorny.models.Shipment;

import java.security.Principal;
import java.util.List;

public interface ShipmentService {
    List<Shipment> getAllShipments();
    void createShipment(Shipment shipment, Principal principal);
    Shipment getById(Long id);
}
