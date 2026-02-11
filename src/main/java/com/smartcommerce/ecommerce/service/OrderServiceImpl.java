package com.smartcommerce.ecommerce.service;

import com.smartcommerce.ecommerce.exceptions.APIException;
import com.smartcommerce.ecommerce.exceptions.ResourceNotFoundException;
import com.smartcommerce.ecommerce.mapper.OrderMapper;
import com.smartcommerce.ecommerce.model.*;
import com.smartcommerce.ecommerce.payload.OrderDTO;
import com.smartcommerce.ecommerce.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    CartRepository cartRepository;
    AddressRepository addressRepository;
    OrderItemRepository orderItemRepository;
    OrderRepository orderRepository;
    PaymentRepository paymentRepository;
    CartItemRepository cartItemRepository;
    OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod,
                               String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        Cart cart = cartRepository.findCartByEmail(emailId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "email", emailId);
        }

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty()) {
            throw new APIException("Cart is empty. Cannot place order.");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        Order order = new Order();
        order.setEmail(emailId);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted");
        order.setAddress(address);

        Payment payment = Payment.builder()
                .paymentMethod(paymentMethod)
                .pgPaymentId(pgPaymentId)
                .pgStatus(pgStatus)
                .pgResponseMessage(pgResponseMessage)
                .pgName(pgName)
                .order(order)
                .build();

        order.setPayment(payment);

        // Lưu Order trước để có ID cho OrderItems
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = cartItems.stream().map(cartItem ->
                OrderItem.builder()
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .discount(cartItem.getDiscount())
                        .orderedProductPrice(cartItem.getProductPrice())
                        .order(savedOrder)
                        .build()
        ).collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        //Dọn dẹp giỏ hàng & Kết thúc
        double totalToClear = cart.getTotalPrice();
        cartItemRepository.deleteAll(cartItems);

        cart.getCartItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);

        return orderMapper.toDTO(savedOrder);
    }
}
