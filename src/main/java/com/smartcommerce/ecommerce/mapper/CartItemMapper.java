package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.CartItem;
import com.smartcommerce.ecommerce.payload.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartItemMapper {

    // Chuyển đổi CartItem thành ProductDTO để khớp với cấu trúc CartDTO
    @Mapping(target = "productId", source = "product.productId")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "image", source = "product.image")
    @Mapping(target = "description", source = "product.description")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "discount", source = "product.discount")
    @Mapping(target = "specialPrice", source = "product.specialPrice")
    @Mapping(target = "quantity", source = "quantity") // Lấy quantity từ CartItem (số lượng trong giỏ)
    ProductDTO toProductDTO(CartItem cartItem);
}
