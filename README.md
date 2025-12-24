# 🛒 smart-commerce
> **Production-grade eCommerce Backend with built-in AI capabilities.**

![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16+-blue?style=for-the-badge&logo=postgresql)
![Spring Security](https://img.shields.io/badge/Spring_Security-7.x-gray?style=for-the-badge&logo=springsecurity)

---

## 🌟 Giới thiệu
**Smart Commerce** là hệ thống Backend thương mại điện tử hiện đại, được tối ưu hóa cho hiệu năng cao nhờ tận dụng **Java 25** và **Spring Boot 4**. Dự án được thiết kế với kiến trúc phân lớp (Layered Architecture) giúp dễ dàng bảo trì và sẵn sàng tích hợp các mô hình AI trong tương lai.

## ✨ Tính năng nổi bật
* **Bảo mật nâng cao:** Xác thực không trạng thái (Stateless) với JWT, lưu trữ an toàn qua **HttpOnly Cookies** để chống XSS
* **Quản lý Catalog:** Hệ thống danh mục và sản phẩm hỗ trợ tìm kiếm nâng cao, phân trang và sắp xếp linh hoạt.
* **Giỏ hàng & Đơn hàng:** Luồng xử lý đặt hàng đảm bảo tính toàn vẹn dữ liệu (Atomic Transactions).
* **Quản lý địa chỉ:** Hỗ trợ người dùng lưu trữ nhiều địa chỉ giao hàng chuyên nghiệp.

## 🛠 Công nghệ sử dụng
* **Core:** Java 25 (LTS), Spring Boot 4.0.1.
* **Database:** PostgreSQL & Spring Data JPA (Hibernate 7).
* **Documentation:** Swagger UI / OpenAPI 3.0 (SpringDoc).
* **Utilities:** ModelMapper, Lombok, Jakarta Validation.

## 📁 Cấu trúc dự án
```text
src/main/java/com/smartcommerce/ecommerce/
├── config/         # Cấu hình hệ thống & Swagger 
├── controller/     # Tầng điều hướng REST API Endpoints 
├── exceptions/     # Xử lý lỗi tập trung & Custom Exceptions 
├── model/          # Các thực thể JPA mapping trực tiếp với Database 
├── payload/        # DTOs (Data Transfer Objects) cho Request/Response 
├── repositories/   # Tầng truy xuất dữ liệu (Spring Data JPA) 
├── security/       # Cấu hình JWT, Security Filters & Authorization 
├── service/        # Tầng xử lý Logic nghiệp vụ & Transactions 
└── util/           # Các lớp tiện ích bổ trợ 