package com.motorny.dto.user;

import com.motorny.validation.ValidPassword;
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
public class UserResetPasswordDto {
    @Email
    private String email;

    @NotBlank(message = "A field with a new password cannot be empty")
    @Size(min = 6, max = 120)
    @ValidPassword
    private String newPassword;
}
