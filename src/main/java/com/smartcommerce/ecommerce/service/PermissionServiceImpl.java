package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.mapper.PermissionMapper;
import com.smartcommerce.ecommerce.model.Permission;
import com.smartcommerce.ecommerce.model.Role;
import com.smartcommerce.ecommerce.payload.PermissionDTO;
import com.smartcommerce.ecommerce.repositories.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    @Transactional
    public PermissionDTO create(PermissionDTO permissionDTO) {
        if (permissionRepository.existsByPermissionName(permissionDTO.getPermissionName())) {
            throw new APIException("Permission already exists: " + permissionDTO.getPermissionName());
        }
        Permission permission = permissionMapper.toEntity(permissionDTO);
        return permissionMapper.toDTO(permissionRepository.save(permission));
    }

    @Override
    public List<PermissionDTO> getAll() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDTO)
                .toList();
    }

    @Override
    public PermissionDTO getById(Integer permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new APIException("Permission not found with id: " + permissionId));
        return permissionMapper.toDTO(permission);
    }

    @Override
    @Transactional
    public void delete(Integer permissionId) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new APIException("Permission not found with id: " + permissionId));

        for (Role role : new HashSet<>(permission.getRoles())) {
            role.getPermissions().remove(permission);
        }

        permissionRepository.delete(permission);
    }
}