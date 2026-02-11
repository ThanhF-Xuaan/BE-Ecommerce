package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Payment;
import com.smartcommerce.ecommerce.payload.PaymentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
    PaymentDTO toDTO(Payment payment);
    Payment toEntity(PaymentDTO paymentDTO);
}
