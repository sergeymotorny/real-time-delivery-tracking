package com.motorny.models;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Shipment shipmentId;

    @Column(name = "location_name")
    private String locationName;


}
