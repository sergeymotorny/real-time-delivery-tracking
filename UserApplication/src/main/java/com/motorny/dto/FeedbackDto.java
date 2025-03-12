package com.motorny.dto;

import com.motorny.models.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDto {
    private Long id;

    private User user;

    @NotNull(message = "The rating cannot be empty")
    @Max(5)
    @Min(0)
    private Integer rating;

    @Length(max = 255, message = "The field cannot be more than 255 characters")
    private String comment;
}
