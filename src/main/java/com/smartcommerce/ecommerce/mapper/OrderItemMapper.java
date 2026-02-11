package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.OrderItem;
import com.smartcommerce.ecommerce.payload.OrderItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    OrderItemDTO toDTO(OrderItem orderItem);

    OrderItem toEntity(OrderItemDTO orderItemDTO);
}
