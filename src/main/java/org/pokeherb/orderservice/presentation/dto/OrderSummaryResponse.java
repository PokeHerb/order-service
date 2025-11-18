package org.pokeherb.orderservice.presentation.dto;

import org.pokeherb.orderservice.domain.Order;
import org.pokeherb.orderservice.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

// 목록용 요약 응답
public record OrderSummaryResponse(
        UUID id,
        UUID productId,
        UUID orderUserId,
        UUID requestVendorId,
        OrderStatus status,
        LocalDateTime dueAt,
        LocalDateTime createdAt,
        String productName,
        int quantity
) {
    public static OrderSummaryResponse from(Order order) {
        return new OrderSummaryResponse(
                order.getId(),
                order.getProductId(),
                order.getOrderUserId(),
                order.getRequestVendorId(),
                order.getOrderStatus(),
                order.getDueAt(),
                order.getCreatedAt(),
                order.getProductName(),
                order.getQuantity()
        );
    }
}