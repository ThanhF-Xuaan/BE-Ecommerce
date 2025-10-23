package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
