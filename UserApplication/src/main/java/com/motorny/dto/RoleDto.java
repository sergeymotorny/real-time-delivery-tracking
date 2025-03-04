package com.motorny.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleDto {

    private Long id;

    @NotBlank
    @Size(max = 30)
    private String name;
}
