package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.AppRole;
import com.smartcommerce.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole appRole);

    @Query("SELECT r " +
            "FROM Role r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE r.roleName = :roleName")
    Optional<Role> findByRoleNameWithPermissions(@Param("roleName") AppRole roleName);
}
