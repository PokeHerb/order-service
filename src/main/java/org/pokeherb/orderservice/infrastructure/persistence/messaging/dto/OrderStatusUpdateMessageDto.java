package org.pokeherb.orderservice.infrastructure.persistence.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderStatusUpdateMessageDto(
        UUID orderId,
        String orderStatus,
        LocalDateTime changedAt
) {
}
