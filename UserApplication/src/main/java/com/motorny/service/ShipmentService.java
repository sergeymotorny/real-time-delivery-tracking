package com.motorny.service;

import com.motorny.models.Shipment;

import java.util.List;

public interface ShipmentService {
    List<Shipment> getAllShipments();
    void create(Shipment shipment);
    Shipment getById(Long id);
}
