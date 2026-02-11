package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.exceptions.ResourceNotFoundException;
import com.smartcommerce.ecommerce.mapper.CartMapper;
import com.smartcommerce.ecommerce.model.Cart;
import com.smartcommerce.ecommerce.model.CartItem;
import com.smartcommerce.ecommerce.model.Product;
import com.smartcommerce.ecommerce.payload.CartDTO;
import com.smartcommerce.ecommerce.repositories.CartItemRepository;
import com.smartcommerce.ecommerce.repositories.CartRepository;
import com.smartcommerce.ecommerce.repositories.ProductRepository;
import com.smartcommerce.ecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    CartItemRepository cartItemRepository;
    AuthUtil authUtil;
    CartMapper cartMapper;

    private Cart getOrCreateCart() {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        if (userCart != null) {
            return userCart;
        }

        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart  = getOrCreateCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem existingItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if (existingItem != null) {
            throw new APIException("Product " + product.getProductName() + " already exists in the cart");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Inventory insufficient. Only " + product.getQuantity() + " units left.");
        }

        CartItem newItem = CartItem.builder()
                .product(product)
                .cart(cart)
                .quantity(quantity)
                .discount(product.getDiscount())
                .productPrice(product.getSpecialPrice())
                .build();

        cartItemRepository.save(newItem);
        cart.getCartItems().add(newItem);

        // Cập nhật tồn kho
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        // Cập nhật tổng tiền giỏ hàng
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.toDTO(updatedCart);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No carts found");
        }

        return carts.stream()
                .map(cartMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }
        return cartMapper.toDTO(cart);
    }


    @Transactional
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantityChange) {
        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getCartId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new APIException("Product not found in cart");
        }

        int newQuantity = cartItem.getQuantity() + quantityChange;

        if (newQuantity < 0) {
            throw new APIException("Quantity cannot be negative");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cart.getCartId(), productId);
            Cart refreshedCart = cartRepository.findById(cart.getCartId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cart.getCartId()));
            return cartMapper.toDTO(refreshedCart);
        } else {
            // Kiểm tra tồn kho khi tăng số lượng
            if (quantityChange > 0 && product.getQuantity() < quantityChange) {
                throw new APIException("Inventory insufficient");
            }

            cartItem.setQuantity(newQuantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantityChange));
            product.setQuantity(product.getQuantity() - quantityChange);

            cartItemRepository.save(cartItem);
            productRepository.save(product);
            Cart updatedCart = cartRepository.save(cart);

            return cartMapper.toDTO(updatedCart);
        }
    }


    @Transactional
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        // Hoàn trả lại số lượng cho kho sản phẩm
        Product product = cartItem.getProduct();
        product.setQuantity(product.getQuantity() + cartItem.getQuantity());
        productRepository.save(product);

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cart.getCartItems().remove(cartItem);

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
        cartRepository.save(cart);

        return "Product " + product.getProductName() + " removed successfully";
    }

    @Override
    @Transactional
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null) return;

        // Tính toán lại giá dựa trên giá mới của sản phẩm (giữ nguyên số lượng)
        double oldItemTotal = cartItem.getProductPrice() * cartItem.getQuantity();
        double newItemTotal = product.getSpecialPrice() * cartItem.getQuantity();

        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() - oldItemTotal + newItemTotal);

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }
}
