package org.pokeherb.orderservice.infrastructure.persistence.messaging.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryCreateEvent(
        UUID orderId,
        UUID productId,
        int quantity,
        UUID orderUserId,
        String productName,
        LocalDateTime dueAt,
        String requestMemo,
        Long startHubId,
        Long endHubId,
        UUID requestVendorId,
        UUID receiveVendorId,
        String vendorAddress,
        UUID receiverSlackId,
        String receiverName,
        LocalDateTime createdAt
) {}
