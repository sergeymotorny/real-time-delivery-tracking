package com.motorny.dto;

import com.motorny.models.Courier;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public class LocationDto {

    private Long id;

    private Double latitude;
    private Double longitude;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private Courier courier;
}
