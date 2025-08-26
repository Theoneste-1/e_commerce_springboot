package com.example.e_commerce.services.order;


import com.example.e_commerce.dto.order.*;
import com.example.e_commerce.exceptions.ResourceNotFoundException;
import com.example.e_commerce.models.order.Order;
import com.example.e_commerce.models.order.OrderItem;
import com.example.e_commerce.models.product.Product;
import com.example.e_commerce.repositories.order.OrderRepository;
import com.example.e_commerce.repositories.product.ProductRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    // Inject PaymentService, ShipmentService for creating associated entities

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        return null;
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findByIdNative(id);
        if (order == null) throw new ResourceNotFoundException("Order not found: " + id);
        return mapToResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrdersByUserId(String userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).map(this::mapToResponse);
    }

    // Update, delete similar
    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getOrder().getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice()
                )).toList();
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                items
        );
    }

    public void deleteOrder(String id) {
    }

    public OrderResponse updateOrder(String id, @Valid OrderUpdateRequest request) {
        return null;
    }
}