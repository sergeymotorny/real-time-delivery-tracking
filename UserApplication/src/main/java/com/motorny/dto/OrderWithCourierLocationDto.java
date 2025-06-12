package com.motorny.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithCourierLocationDto {
    private OrderDto orderDto;
    private Double courierLatitude;
    private Double courierLongitude;
}
