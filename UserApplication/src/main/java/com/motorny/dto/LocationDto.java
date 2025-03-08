package com.motorny.dto;

import com.motorny.models.Courier;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class LocationDto {

    private Long id;

    private Double latitude;
    private Double longitude;

    private LocalDateTime timestamp;

    private Courier courier;
}
