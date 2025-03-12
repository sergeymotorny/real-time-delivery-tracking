package com.motorny.models;

import com.motorny.models.enums.DeliveryType;
import com.motorny.models.enums.ShipmentPaymentMethod;
import com.motorny.models.enums.ShipmentStatus;
import com.motorny.models.enums.ShipmentType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Generated(event = EventType.INSERT)
    @Column(name = "tracking_number",
            columnDefinition = "serial", unique = true, nullable = false, insertable = false, updatable = false)
    private Long trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @Column(name = "receiver_name", nullable = false)
    private String receiverFullName;

    @Column(name = "estimated_delivery", nullable = false)
    private LocalDateTime estimatedDelivery;

    @Column(name = "receiver_address", nullable = false)
    private String receiverAddress;

    @Column(name = "receiver_phone", nullable = false)
    private String receiverPhone;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ShipmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false)
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private ShipmentPaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_type", nullable = false)
    private ShipmentType shipmentType;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;
}
