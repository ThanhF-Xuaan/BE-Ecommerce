package com.smartcommerce.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customAPI(){
        //phương thức xác thực
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer") // Sử dụng cơ chế Bearer
                .bearerFormat("JWT") // Định dạng token là JWT
                .description("JWT Bearer Token");

        //Áp dụng phương thức xác thực
        SecurityRequirement bearerRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        return new OpenAPI()
                .info(new Info()
                        .title("Smart Commerce API System")
                        .version("1.0")
                        .description("Hệ thống API cho dự án E-commerce sử dụng Java 25 và Spring Boot 4.0.1"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", bearerScheme)) // Đăng ký scheme vào danh sách component
                .addSecurityItem(bearerRequirement); // Áp dụng scheme này ở cấp độ toàn cầu (Global)
    }
}
