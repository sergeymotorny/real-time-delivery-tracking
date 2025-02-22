package com.motorny.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The field trackingNumber cannot be empty")
    @Column(name = "tracking_number", unique = true, nullable = false)
    private Long trackingNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;

    @NotEmpty(message = "The name of the sender cannot be empty")
    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @FutureOrPresent(message = "Delivery date cannot be in the past")
    @Column(name = "estimated_delivery", nullable = false)
    private LocalDateTime estimatedDelivery;

    @NotEmpty(message = "The receiverAddress of the sender cannot be empty")
    @Column(name = "receiver_address", nullable = false)
    private String receiverAddress;

    @Pattern(regexp = "")
    @Column(name = "receiver_phone", nullable = false)
    private String receiverPhone;

    @Size(min = 0, max = 255, message = "The description should be no more than 255 characters")
    @Column(name = "description")
    private String description;

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

    @NotNull(message = "A field with a parcel weight cannot be empty")
    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt; // Delete in HTML and make automatic

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt; // Delete in HTML and make automatic

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private Courier courier;

    @OneToOne(mappedBy = "shipment")
    private Feedback feedback;

}
