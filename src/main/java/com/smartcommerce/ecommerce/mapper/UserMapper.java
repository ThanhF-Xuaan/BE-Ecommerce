package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Role;
import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.security.services.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userId", target = "id")
    @Mapping(source = "userName", target = "username")
    @Mapping(target = "authorities", expression = "java(mapRolesToAuthorities(user.getRoles()))")
    UserDetailsImpl toUserDetails(User user);

    default Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        // 1. Lấy danh sách Roles (đã có tiền tố ROLE_)
        List<String> roleList = roles.stream()
                .map(role -> role.getRoleName().name())
                .sorted()
                .toList();

        // 2. Lấy danh sách Permissions (SCREAMING_SNAKE_CASE)
        List<String> permissionList = roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(p -> p.getPermissionName().toUpperCase())
                .distinct()
                .sorted()
                .toList();

        return Stream.concat(roleList.stream(), permissionList.stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
