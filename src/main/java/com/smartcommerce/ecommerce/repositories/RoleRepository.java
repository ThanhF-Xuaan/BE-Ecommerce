package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.AppRole;
import com.smartcommerce.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
