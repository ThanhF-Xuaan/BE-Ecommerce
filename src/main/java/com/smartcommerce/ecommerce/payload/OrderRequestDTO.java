package com.smartcommerce.ecommerce.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequestDTO {
    Long addressId;
    String paymentMethod;
    String pgName;
    String pgPaymentId;
    String pgStatus;
    String pgResponseMessage;
}