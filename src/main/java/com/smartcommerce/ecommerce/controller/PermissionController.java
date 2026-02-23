package com.smartcommerce.ecommerce.controller;

import com.smartcommerce.ecommerce.payload.PermissionDTO;
import com.smartcommerce.ecommerce.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Permission Management", description = "Dành riêng cho ADMIN quản lý các quyền hạn hệ thống")
@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasAuthority('MANAGE_SECURITY')")
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        return new ResponseEntity<>(permissionService.create(permissionDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Integer id) {
        return ResponseEntity.ok(permissionService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePermission(@PathVariable Integer id) {
        permissionService.delete(id);
        return ResponseEntity.ok("Permission deleted successfully with id: " + id);
    }
}
