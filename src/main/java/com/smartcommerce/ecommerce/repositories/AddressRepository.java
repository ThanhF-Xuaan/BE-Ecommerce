package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByCityAndWardAndStreetDetailAndBuildingNameAndPincode
            (String city, String ward, String streetDetail, String buildingName, String pinCode);
}
