package com.motorny.dto;

import com.motorny.models.Role;
import com.motorny.models.enums.Status;
import com.motorny.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "The name cannot be empty")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "The surname cannot be empty")
    @Size(max = 50)
    private String lastName;

    @ValidPhone
    private String phone;

    @Email
    private String email;

    @NotBlank(message = "A password field cannot be empty")
    @Size(min = 6, max = 120)
    private String password;

    private LocalDate createdAt;

    private Status status;

    private Set<Role> roles;
}

