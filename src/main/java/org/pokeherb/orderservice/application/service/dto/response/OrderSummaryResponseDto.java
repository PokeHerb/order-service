package org.pokeherb.orderservice.application.service.dto.response;

import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

// 목록용 요약 응답
public record OrderSummaryResponseDto(
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
    public static OrderSummaryResponseDto from(Order order) {
        return new OrderSummaryResponseDto(
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