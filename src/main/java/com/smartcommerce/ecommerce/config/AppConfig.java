package com.smartcommerce.ecommerce.config;

import com.smartcommerce.ecommerce.model.AppRole;
import com.smartcommerce.ecommerce.model.Permission;
import com.smartcommerce.ecommerce.model.Role;
import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.repositories.PermissionRepository;
import com.smartcommerce.ecommerce.repositories.RoleRepository;
import com.smartcommerce.ecommerce.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initData() {
        return args -> initializeSecurityData();
    }

    @Transactional
    public void initializeSecurityData() {
        // --- 1. NHÓM QUYỀN CATEGORY ---
        Permission pCreateCat = getOrSaveP("CREATE_CATEGORY");
        Permission pUpdateCat = getOrSaveP("UPDATE_CATEGORY");
        Permission pDeleteCat = getOrSaveP("DELETE_CATEGORY");

        // --- 2. NHÓM QUYỀN PRODUCT ---
        Permission pCreateProd = getOrSaveP("CREATE_PRODUCT");
        Permission pUpdateProd = getOrSaveP("UPDATE_PRODUCT");
        Permission pDeleteProd = getOrSaveP("DELETE_PRODUCT");
        Permission pUploadProdImg = getOrSaveP("UPLOAD_IMAGE");

        // --- 3. NHÓM QUYỀN CART & ORDER ---
        Permission pReadCart = getOrSaveP("READ_CART");

        // --- 4. NHÓM QUYỀN USER & SECURITY ---
        Permission pAddress = getOrSaveP("MANAGE_ADDRESS");
        Permission pSecurity = getOrSaveP("MANAGE_SECURITY");

        // --- KHỞI TẠO ROLES VÀ GÁN QUYỀN ---

        // ROLE_USER: Người mua hàng
        Role userRole = getOrSaveR(AppRole.ROLE_USER, new HashSet<>(Arrays.asList(

        )));

        // ROLE_SELLER: Người bán hàng (Bao gồm quyền của User + Quản lý Product)
        Role sellerRole = getOrSaveR(AppRole.ROLE_SELLER, new HashSet<>(Arrays.asList(
                pCreateProd, pUpdateProd, pDeleteProd, pUploadProdImg
        )));

        // ROLE_ADMIN: Toàn quyền
        Role adminRole = getOrSaveR(AppRole.ROLE_ADMIN, new HashSet<>(Arrays.asList(
                pCreateCat, pUpdateCat, pDeleteCat,
                pCreateProd, pUpdateProd, pDeleteProd, pUploadProdImg,
                pReadCart,
                pSecurity, pAddress
        )));

        // --- KHỞI TẠO USERS ---
        createInitialUser("user1", "user1@gmail.com", "password", new HashSet<>(Arrays.asList(userRole)));
        createInitialUser("seller1", "seller1@gmail.com", "password", new HashSet<>(Arrays.asList(sellerRole)));
        createInitialUser("admin", "admin@gmail.com", "admin", new HashSet<>(Arrays.asList(adminRole)));
    }

    private void createInitialUser(String username, String email, String password, Set<Role> roles) {
        if (!userRepository.existsByUserName(username)) {
            User user = new User(username, email, passwordEncoder.encode(password));
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    private Permission getOrSaveP(String name) {
        return permissionRepository.findByPermissionName(name)
                .orElseGet(() -> permissionRepository.save(Permission.builder().permissionName(name).build()));
    }

    private Role getOrSaveR(AppRole roleName, Set<Permission> permissions) {
        Role role = roleRepository.findByRoleName(roleName).orElseGet(() -> new Role(roleName));
        role.setPermissions(permissions);
        return roleRepository.save(role);
    }
}
