package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.model.AppRole;
import com.smartcommerce.ecommerce.model.Role;
import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.repositories.RoleRepository;
import com.smartcommerce.ecommerce.repositories.UserRepository;
import com.smartcommerce.ecommerce.security.jwt.JwtUtils;
import com.smartcommerce.ecommerce.security.request.LoginRequest;
import com.smartcommerce.ecommerce.security.request.SignupRequest;
import com.smartcommerce.ecommerce.security.response.MessageResponse;
import com.smartcommerce.ecommerce.security.response.UserInfoResponse;
import com.smartcommerce.ecommerce.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtils jwtUtils;

    @Override
    public UserInfoResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new UserInfoResponse(userDetails.getId().toString(), userDetails.getUsername(),
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }

    @Override
    public ResponseCookie generateJwtCookie(UserInfoResponse userInfo) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String jwt = jwtUtils.generateTokenFromUserDetails(userDetails);
        return jwtUtils.createCookie(jwt);
    }

    @Override
    public ResponseCookie signoutUser() {
        return jwtUtils.deleteCookie();
    }

    @Override
    @Transactional
    public MessageResponse registerUser(SignupRequest req) {
        if (userRepository.existsByUserName(req.getUsername())) throw new RuntimeException("Username taken");
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new APIException("Error: Email is already in use!");
        }

        User user = new User(req.getUsername(), req.getEmail(), encoder.encode(req.getPassword()));
        Set<Role> roles = new HashSet<>();

        req.getRole().forEach(r -> {
            AppRole appRole = switch (r.toLowerCase()) {
                case "admin" -> AppRole.ROLE_ADMIN;
                case "seller" -> AppRole.ROLE_SELLER;
                default -> AppRole.ROLE_USER;
            };
            roles.add(roleRepository.findByRoleName(appRole).orElseThrow()); //
        });

        user.setRoles(roles);
        userRepository.save(user);
        return new MessageResponse("User registered successfully!");
    }
}
