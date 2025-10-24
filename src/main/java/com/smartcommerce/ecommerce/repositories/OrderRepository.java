package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}