package org.pokeherb.orderservice.application.service.dto.request;

import org.pokeherb.orderservice.domain.entity.OrderStatus;

import java.util.UUID;

public record OrderSearchConditionRequestDto(
        UUID orderUserId,
        UUID requestVendorId,
        UUID productId,
        String productName,
        OrderStatus status
) { }
