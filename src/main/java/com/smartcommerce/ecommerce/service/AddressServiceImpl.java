package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.exceptions.ResourceNotFoundException;
import com.smartcommerce.ecommerce.mapper.AddressMapper;
import com.smartcommerce.ecommerce.model.Address;
import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.payload.AddressDTO;
import com.smartcommerce.ecommerce.repositories.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{
    AddressRepository addressRepository;
    AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Address address = addressMapper.toEntity(addressDTO);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDTO(savedAddress);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addressMapper.toDTOList(addresses);
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return addressMapper.toDTO(address);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        return addressMapper.toDTOList(user.getAddresses());
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressMapper.updateEntityFromDTO(addressDTO, addressFromDatabase);

        Address updatedAddress = addressRepository.save(addressFromDatabase);
        return addressMapper.toDTO(updatedAddress);
    }

    @Override
    @Transactional
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressRepository.delete(addressFromDatabase);
        return "Address deleted successfully with addressId: " + addressId;
    }

    @Override
    @Transactional
    public String deleteAddressByUser(Long addressId, User user) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new APIException("Access Denied: You do not own this address!");
        }

        addressRepository.delete(address);
        return "Address deleted successfully with addressId: " + addressId;
    }
}
