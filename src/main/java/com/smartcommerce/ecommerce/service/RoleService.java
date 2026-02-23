package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.payload.RoleDTO;

import java.util.List;

public interface RoleService {
    RoleDTO create(RoleDTO roleDTO);

    List<RoleDTO> getAll();

    RoleDTO getById(Integer roleId);

    RoleDTO update(Integer roleId, RoleDTO roleDTO);

    void delete(Integer roleId);

    RoleDTO addPermissionToRole(Integer roleId, Integer permissionId);

    RoleDTO removePermissionFromRole(Integer roleId, Integer permissionId);
}
