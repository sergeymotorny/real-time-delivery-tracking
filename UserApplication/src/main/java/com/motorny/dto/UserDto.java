package com.motorny.dto;

import com.motorny.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "The name cannot be empty")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "The surname cannot be empty")
    @Size(max = 50)
    private String lastName;

    @NotNull(message = "Phone number cannot be null")
    @ValidPhone
    private String phone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Incorrect mail format!")
    private String email;
}

