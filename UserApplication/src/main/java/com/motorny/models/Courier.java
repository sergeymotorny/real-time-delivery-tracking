package com.motorny.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "couriers")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "courier")
    private List<Shipment> shipments;

    @OneToMany(mappedBy = "courier")
    private List<Location> location;
}


