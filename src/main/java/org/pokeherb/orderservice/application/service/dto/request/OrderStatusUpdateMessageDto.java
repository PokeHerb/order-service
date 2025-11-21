package org.pokeherb.orderservice.application.service.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderStatusUpdateMessageDto(
        UUID orderId,
        String status,             // "CREATED", "ASSIGNED", "PICKED_UP", ...
        UUID deliveryDriverId,
        String actorRole,          // "HUB_MANAGER", "COURIER", "ADMIN" 등 (추후 권한 체크용)
        LocalDateTime changedAt
) { }