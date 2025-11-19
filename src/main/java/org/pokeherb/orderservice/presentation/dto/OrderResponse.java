package org.pokeherb.orderservice.presentation.dto;

import org.pokeherb.orderservice.domain.Order;
import org.pokeherb.orderservice.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

// 상세 조회 응답
public record OrderResponse(
        UUID id,
        UUID productId,
        UUID orderUserId,
        UUID requestVendorId,
        UUID deliveryDriverId,
        OrderStatus status,
        LocalDateTime dueAt,
        LocalDateTime cancelledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt,
        String productName,
        int quantity,
        String requestMemo
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getOrderUserId(),
                order.getRequestVendorId(),
                order.getDeliveryDriverId(),
                order.getOrderStatus(),
                order.getDueAt(),
                order.getCancelledAt(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getDeletedAt(),
                order.getProductName(),
                order.getQuantity(),
                order.getRequestMemo()
        );
    }
}