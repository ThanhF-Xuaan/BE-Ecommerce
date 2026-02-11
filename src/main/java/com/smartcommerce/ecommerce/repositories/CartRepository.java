package com.smartcommerce.ecommerce.repositories;

import com.smartcommerce.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Override
    @EntityGraph(attributePaths = {"user", "cartItems", "cartItems.product"})
    List<Cart> findAll();

    @Query("SELECT c " +
            "FROM Cart c " +
            "LEFT JOIN FETCH c.cartItems ci L" +
            "EFT JOIN FETCH ci.product " +
            "WHERE c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c " +
            "FROM Cart c " +
            "LEFT JOIN FETCH c.cartItems ci " +
            "LEFT JOIN FETCH ci.product " +
            "WHERE c.user.email = ?1 AND c.cartId = ?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);

    @Query("SELECT DISTINCT c " +
            "FROM Cart c " +
            "JOIN FETCH c.cartItems ci " +
            "JOIN FETCH ci.product p " +
            "WHERE p.productId = ?1")
    List<Cart> findCartsByProductId(Long productId);
}