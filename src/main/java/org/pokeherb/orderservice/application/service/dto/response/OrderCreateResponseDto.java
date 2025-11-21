package org.pokeherb.orderservice.application.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.pokeherb.orderservice.domain.entity.Order;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class OrderCreateResponseDto {
    private UUID orderId;
    private String productName;
    private int quantity;
    private String status;
    private LocalDateTime dueAt;
    private LocalDateTime createdAt;

    public static OrderCreateResponseDto from(Order order) {
        return OrderCreateResponseDto.builder()
                .orderId(order.getId())
                .productName(order.getProductName())
                .quantity(order.getQuantity())
                .status(order.getOrderStatus().name())
                .dueAt(order.getDueAt())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
