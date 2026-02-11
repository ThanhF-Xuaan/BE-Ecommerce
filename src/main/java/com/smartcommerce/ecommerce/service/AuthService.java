package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.security.request.LoginRequest;
import com.smartcommerce.ecommerce.security.request.SignupRequest;
import com.smartcommerce.ecommerce.security.response.MessageResponse;
import com.smartcommerce.ecommerce.security.response.UserInfoResponse;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    UserInfoResponse authenticateUser(LoginRequest loginRequest);
    ResponseCookie generateJwtCookie(UserInfoResponse userPrincipal);
    MessageResponse registerUser(SignupRequest signUpRequest);
    ResponseCookie signoutUser();
}
