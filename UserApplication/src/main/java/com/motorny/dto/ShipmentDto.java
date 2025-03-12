package com.motorny.dto;

import com.motorny.models.Courier;
import com.motorny.models.Feedback;
import com.motorny.models.enums.DeliveryType;
import com.motorny.models.enums.ShipmentPaymentMethod;
import com.motorny.models.enums.ShipmentType;
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
public class ShipmentDto {

    private Long id;

    @NotEmpty(message = "The name of the sender cannot be empty")
    private String receiverFullName;

    @FutureOrPresent(message = "Delivery date cannot be in the past")
    private LocalDateTime estimatedDelivery;

    @NotEmpty(message = "The receiverAddress of the sender cannot be empty")
    private String receiverAddress;

    @ValidPhone
    private String receiverPhone;

    @Size(min = 0, max = 255, message = "The description should be no more than 255 characters")
    private String description;

    @NotNull(message = "Choose the type of delivery!")
    private DeliveryType deliveryType;

    @NotNull(message = "The type of payment must be selected!")
    private ShipmentPaymentMethod paymentMethod;

    @NotNull(message = "The type of shipment must be selected!")
    private ShipmentType shipmentType;

    @NotNull(message = "A field with a parcel weight cannot be empty")
    private Double weight;

    private Courier courier;

    private Feedback feedback;
}
