package org.pokeherb.orderservice.infrastructure.persistence.messaging.dto;

import java.util.UUID;

// messaging 패키지 쪽에 두는 걸 추천
public record OrderCancelMessageDto(
        UUID orderId,
        UUID cancellerId
) {}