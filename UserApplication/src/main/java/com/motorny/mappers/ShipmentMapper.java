package com.motorny.mappers;

import com.motorny.dto.ShipmentDto;
import com.motorny.models.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "receiverFullName", source = "order.receiverFullName")
    @Mapping(target = "receiverAddress", source = "order.receiverAddress")
    @Mapping(target = "receiverPhone", source = "order.receiverPhone")
    ShipmentDto toShipmentDto(Shipment shipment);

    Shipment toShipment(ShipmentDto shipmentDto);
}
