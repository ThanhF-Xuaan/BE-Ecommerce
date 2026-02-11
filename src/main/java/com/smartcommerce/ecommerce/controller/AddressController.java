package com.smartcommerce.ecommerce.controller;

import com.smartcommerce.ecommerce.model.User;
import com.smartcommerce.ecommerce.payload.AddressDTO;
import com.smartcommerce.ecommerce.service.AddressService;
import com.smartcommerce.ecommerce.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController implements AddressApi{
    AuthUtil authUtil;
    AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        User user = authUtil.loggedInUser();
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses(){
        return new ResponseEntity<>(addressService.getAddresses(), HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        return new ResponseEntity<>(addressService.getAddressesById(addressId), HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getCurrentUserAddresses(){
        User user = authUtil.loggedInUser();
        return new ResponseEntity<>(addressService.getUserAddresses(user), HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                    @Valid @RequestBody AddressDTO addressDTO){
        return new ResponseEntity<>(addressService.updateAddress(addressId, addressDTO), HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressByAdmin(@PathVariable Long addressId){
        return new ResponseEntity<>(addressService.deleteAddress(addressId), HttpStatus.OK);
    }

    @DeleteMapping("/users/addresses/{addressId}")
    public ResponseEntity<String> deleteAddressOfCurrentUser(@PathVariable Long addressId){
        User user = authUtil.loggedInUser();
        return new ResponseEntity<>(addressService.deleteAddressByUser(addressId, user), HttpStatus.OK);
    }
}
