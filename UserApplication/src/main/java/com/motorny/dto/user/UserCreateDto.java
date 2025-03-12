package com.motorny.dto.user;

import com.motorny.validation.ValidPassword;
import com.motorny.validation.ValidPhone;
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
public class UserCreateDto {

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
}
