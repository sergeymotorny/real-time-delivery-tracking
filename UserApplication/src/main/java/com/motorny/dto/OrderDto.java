package com.motorny.dto;

import com.motorny.models.Shipment;
import com.motorny.models.User;
import com.motorny.models.enums.DeliveryType;
import com.motorny.models.enums.OrderPaymentMethod;
import com.motorny.models.enums.OrderStatus;
import com.motorny.models.enums.OrderType;
import com.motorny.validation.ValidPhone;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotEmpty(message = "The name of the sender cannot be empty")
    private String receiverFullName;

    @NotEmpty(message = "The receiverAddress of the sender cannot be empty")
    private String receiverAddress;

    private Double latitude;
    private Double longitude;

    @ValidPhone
    private String receiverPhone;

    @Size(min = 0, max = 255, message = "The description should be no more than 255 characters")
    private String description;

    @FutureOrPresent(message = "Delivery date cannot be in the past")
    private LocalDateTime estimatedDelivery;

    @NotNull(message = "Choose the type of delivery!")
    private DeliveryType deliveryType;

    @NotNull(message = "The type of payment must be selected!")
    private OrderPaymentMethod paymentMethod;

    @NotNull(message = "The type of shipment must be selected!")
    private OrderType orderType;

    @NotNull(message = "A field with a parcel weight cannot be empty")
    private Double weight;

    private OrderStatus status = OrderStatus.CREATED;

    private boolean courierAssigned;

    private Shipment shipment;

    private User customer;
}
