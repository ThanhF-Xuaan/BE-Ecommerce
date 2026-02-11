package com.smartcommerce.ecommerce.mapper;

import com.smartcommerce.ecommerce.model.Address;
import com.smartcommerce.ecommerce.payload.AddressDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toEntity(AddressDTO addressDTO);

    AddressDTO toDTO(Address address);

    List<AddressDTO> toDTOList(List<Address> addresses);

    // nullValuePropertyMappingStrategy = IGNORE: Nếu trường DTO là null thì giữ nguyên giá trị cũ của Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(AddressDTO addressDTO, @MappingTarget Address address);
}
