package com.motorny.dto.user;

import com.motorny.validation.ValidPassword;
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
public class UserUpdatePasswordDto {

    @NotBlank(message = "A field with a old password cannot be empty")
    @Size(min = 6, max = 120)
    private String oldPassword;

    @NotBlank(message = "A field with a new password cannot be empty")
    @Size(min = 6, max = 120)
    @ValidPassword
    private String newPassword;
}
