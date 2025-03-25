package com.motorny.mappers;

import com.motorny.dto.RoleDto;
import com.motorny.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RoleMapper {

    RoleDto toRoleDto(Role role);

    Role toRole(RoleDto roleDto);
}
