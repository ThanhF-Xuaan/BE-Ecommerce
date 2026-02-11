package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Cart;
import com.smartcommerce.ecommerce.payload.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        uses = {CartItemMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {

    @Mapping(target = "products", source = "cartItems")
    CartDTO toDTO(Cart cart);
}
