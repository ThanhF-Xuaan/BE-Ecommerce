package com.smartcommerce.ecommerce.security.response;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoResponse {
    private String id;
    private String jwtToken;

    private String username;
    private List<String> roles;

    public UserInfoResponse(String id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public  UserInfoResponse(String id, String username, List<String> roles, String jwtToken) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }
}
