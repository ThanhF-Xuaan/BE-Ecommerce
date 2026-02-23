package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Permission;
import com.smartcommerce.ecommerce.payload.PermissionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionDTO toDTO(Permission permission);

    Permission toEntity(PermissionDTO dto);
}
