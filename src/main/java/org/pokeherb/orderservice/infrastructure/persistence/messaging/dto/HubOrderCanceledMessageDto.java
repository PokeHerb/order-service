package org.pokeherb.orderservice.infrastructure.persistence.messaging.dto;

import java.util.UUID;

public record HubOrderCanceledMessageDto(
        UUID orderId,
        String productName,
        int quantity
) {}
