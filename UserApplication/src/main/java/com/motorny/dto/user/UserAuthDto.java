package com.motorny.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDto {

    @Email
    private String email;

    @NotBlank(message = "A password field cannot be empty")
    @Size(min = 6, max = 120)
    private String password;
}
