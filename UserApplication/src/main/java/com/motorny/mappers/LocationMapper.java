package com.motorny.mappers;

import com.motorny.dto.LocationDto;
import com.motorny.models.Location;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LocationMapper {

    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);
}
