package com.motorny.mappers;

import com.motorny.dto.RoleDto;
import com.motorny.models.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toRoleDto(Role role);

    Role toRole(RoleDto roleDto);
}
