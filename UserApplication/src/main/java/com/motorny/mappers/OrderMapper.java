package com.motorny.mappers;

import com.motorny.dto.OrderDto;
import com.motorny.dto.admin.AdminOrderDto;
import com.motorny.dto.courier.CourierOrderDto;
import com.motorny.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        imports = com.motorny.models.enums.OrderStatus.class)
public interface OrderMapper {

    @Mapping(
            target = "courierAssigned",
            expression = "java(order.getShipment() != null && order.getShipment().getCourier() != null)")
    OrderDto toOrderDto(Order order);

    @Mapping(target = "status",
            expression = "java(orderDto.getStatus() != null ? orderDto.getStatus() : OrderStatus.CREATED)")
    Order toOrder(OrderDto orderDto);
    AdminOrderDto toAdminOrderDto(Order order);
    CourierOrderDto toCourierOrderDto(Order order);
}
