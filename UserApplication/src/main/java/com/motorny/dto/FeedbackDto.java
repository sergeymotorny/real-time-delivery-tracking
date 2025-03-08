package com.motorny.dto;

import com.motorny.models.Shipment;
import com.motorny.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackDto {
    private Long id;

    private Shipment shipment;

    private User user;

    @NotNull
    @Max(5)
    @Min(0)
    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;
}
