package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.mapper.RoleMapper;
import com.smartcommerce.ecommerce.model.AppRole;
import com.smartcommerce.ecommerce.model.Permission;
import com.smartcommerce.ecommerce.model.Role;
import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.payload.RoleDTO;
import com.smartcommerce.ecommerce.repositories.PermissionRepository;
import com.smartcommerce.ecommerce.repositories.RoleRepository;
import com.smartcommerce.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    UserRepository userRepository;

    @Override
    @Transactional
    public RoleDTO create(RoleDTO roleDTO) {
        AppRole appRole = AppRole.valueOf(roleDTO.getRoleName());
        if (roleRepository.findByRoleName(appRole).isPresent()) {
            throw new APIException("Role already exists: " + roleDTO.getRoleName());
        }

        Role role = roleMapper.toEntity(roleDTO);
        role.setRoleName(appRole);
        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    public List<RoleDTO> getAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDTO)
                .toList();
    }

    @Override
    public RoleDTO getById(Integer roleId) {
        return roleRepository.findById(roleId)
                .map(roleMapper::toDTO)
                .orElseThrow(() -> new APIException("Role not found with id: " + roleId));
    }

    @Override
    @Transactional
    public RoleDTO addPermissionToRole(Integer roleId, Integer permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new APIException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new APIException("Permission not found"));

        role.getPermissions().add(permission); // Hibernate tự xử lý bảng trung gian
        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO removePermissionFromRole(Integer roleId, Integer permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new APIException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new APIException("Permission not found"));

        role.getPermissions().remove(permission);
        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void delete(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new APIException("Role not found with id: " + roleId));

        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getRoles().contains(role)) {
                user.getRoles().remove(role);
                userRepository.save(user);
            }
        }

        role.getPermissions().clear();
        roleRepository.save(role);

        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public RoleDTO update(Integer roleId, RoleDTO roleDTO) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new APIException("Role not found"));

        if (roleDTO.getPermissions() != null) {
            Set<Permission> permissions = roleDTO.getPermissions().stream()
                    .map(p -> permissionRepository.findById(p.getPermissionId())
                            .orElseThrow(() -> new APIException("Permission not found: " + p.getPermissionId())))
                    .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        return roleMapper.toDTO(roleRepository.save(role));
    }
}
