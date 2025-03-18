package com.motorny.mappers;

import com.motorny.dto.OrderDto;
import com.motorny.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrderMapper {

    OrderDto toOrderDto(Order order);

    Order toOrder(OrderDto orderDto);
}
