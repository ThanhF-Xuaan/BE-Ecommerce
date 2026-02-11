package com.smartcommerce.ecommerce.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDTO {
    UUID paymentId;

    @NotBlank
    @Size(min = 4, message = "Payment method must contain at least 4 characters")
    String paymentMethod;
    String pgPaymentId;
    String pgStatus;
    String pgResponseMessage;
    String pgName;
}
