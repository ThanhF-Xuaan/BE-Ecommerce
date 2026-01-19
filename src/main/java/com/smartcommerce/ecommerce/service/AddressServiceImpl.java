package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.ResourceNotFoundException;
import com.smartcommerce.ecommerce.model.Address;
import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.payload.AddressDTO;
import com.smartcommerce.ecommerce.repositories.AddressRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        Optional<Address> existingAddress =
                addressRepository.findAddressByCityAndWardAndStreetDetailAndBuildingNameAndPincode
                (addressDTO.getCity(), addressDTO.getWard(), addressDTO.getStreetDetail(),
                        addressDTO.getBuildingName(), addressDTO.getPincode());

        if(existingAddress.isPresent()){
            existingAddress.get().setUser(user);
            return modelMapper.map(existingAddress.get(), AddressDTO.class);
        }

        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        List<Address> addressesList = user.getAddresses();
        addressesList.add(address);
        user.setAddresses(addressesList);

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(User user) {
        List<Address> addresses = user.getAddresses();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressFromDatabase.setCity(addressDTO.getCity());
        addressFromDatabase.setWard(addressDTO.getWard());
        addressFromDatabase.setStreetDetail(addressDTO.getStreetDetail());
        addressFromDatabase.setBuildingName(addressDTO.getBuildingName());
        addressFromDatabase.setPincode(addressDTO.getPincode());

        Address updatedAddress = addressRepository.save(addressFromDatabase);

        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
    }

    @Override
    public String deleteAddressByUser(Long addressId, User user) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
    }
}
