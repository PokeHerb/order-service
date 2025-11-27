package org.pokeherb.orderservice.infrastructure.persistence.messaging.dto;

import java.util.UUID;

public record DeliveryCreateMessageDto(
        UUID orderId
) {}
