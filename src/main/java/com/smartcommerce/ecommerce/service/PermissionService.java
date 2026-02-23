package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.payload.PermissionDTO;

import java.util.List;

public interface PermissionService {
    PermissionDTO create(PermissionDTO permissionDTO);

    List<PermissionDTO> getAll();

    PermissionDTO getById(Integer permissionId);

    void delete(Integer permissionId);
}
