package com.smartcommerce.ecommerce.payload;

import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {
    UUID orderId;

    @Email
    String email;
    List<OrderItemDTO> orderItems;
    LocalDate orderDate;
    PaymentDTO payment;
    Double totalAmount;
    String orderStatus;
    Long addressId;
}
