package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Order;
import com.smartcommerce.ecommerce.payload.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {OrderItemMapper.class, PaymentMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "addressId", source = "address.addressId")
    OrderDTO toDTO(Order order);

    @Mapping(target = "address", ignore = true)
    Order toEntity(OrderDTO orderDTO);
}