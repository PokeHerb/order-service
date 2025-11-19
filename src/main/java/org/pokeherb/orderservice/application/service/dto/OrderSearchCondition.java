package org.pokeherb.orderservice.application.service.dto;

import org.pokeherb.orderservice.domain.OrderStatus;

import java.util.UUID;

public record OrderSearchCondition(
        UUID orderUserId,
        UUID requestVendorId,
        UUID productId,
        String productName,
        OrderStatus status
) { }
