package com.smartcommerce.ecommerce.security.jwt;

import com.smartcommerce.ecommerce.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtUtils {
    @Value("${spring.app.jwtSecret}")
    String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    int jwtExpirationMs;

    @Value("${spring.ecom.app.jwtCookieName}")
    String jwtCookieName;

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getJwtFromHeaderOrCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName); //
        if (cookie != null) return cookie.getValue();

        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) return bearerToken.substring(7);
        return null;
    }

    public String generateTokenFromUserDetails(UserDetailsImpl userPrincipal) {
        List<String> authorities = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("scopes", authorities) // Gộp tất cả vào đây
                .issuer("THANHF_XUAAN")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), Jwts.SIG.HS512)
                .compact();
    }

    @SuppressWarnings("unchecked")
    public List<String> getScopesFromJwtToken(String token) {
        return (List<String>) getClaims(token).get("scopes");
    }



    public ResponseCookie createCookie(String jwt) {
        return ResponseCookie.from(jwtCookieName, jwt)
                .path("/api").maxAge(24 * 60 * 60)
                .httpOnly(true).secure(false).sameSite("Lax").build();
    }

    public ResponseCookie deleteCookie() {
        return ResponseCookie.from(jwtCookieName, "").path("/api").maxAge(0).build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            log.error("JWT Validation failed: {}", e.getMessage(), e);
        }
        return false;
    }
}
