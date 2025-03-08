package com.motorny.mappers;

import com.motorny.dto.ShipmentDto;
import com.motorny.models.Shipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    ShipmentDto toShipmentDto(Shipment shipment);

    Shipment toShipment(ShipmentDto shipmentDto);
}
