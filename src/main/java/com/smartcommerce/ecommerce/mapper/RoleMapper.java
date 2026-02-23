package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Role;
import com.smartcommerce.ecommerce.payload.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    RoleDTO toDTO(Role role);

    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleDTO roleDTO);
}
