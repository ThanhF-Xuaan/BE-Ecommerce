package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressesById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);

    String deleteAddressByUser(Long addressId, User user);
}
