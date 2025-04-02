package com.motorny.mappers;

import com.motorny.dto.CourierDto;
import com.motorny.models.Courier;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CourierMapper {
    CourierDto toCourierDto(Courier courier);

    Courier toCourier(CourierDto courierDto);
}
