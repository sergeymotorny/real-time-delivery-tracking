package com.motorny.mappers;

import com.motorny.dto.ShipmentDto;
import com.motorny.models.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ShipmentMapper {

    ShipmentDto toShipmentDto(Shipment shipment);

    Shipment toShipment(ShipmentDto shipmentDto);
}
