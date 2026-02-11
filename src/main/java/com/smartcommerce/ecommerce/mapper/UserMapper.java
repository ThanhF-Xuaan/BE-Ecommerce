package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Role;
import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.security.services.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userId", target = "id")
    @Mapping(source = "userName", target = "username")
    @Mapping(target = "authorities", expression = "java(mapRolesToAuthorities(user.getRoles()))")
    UserDetailsImpl toUserDetails(User user);

    default Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
    }
}
