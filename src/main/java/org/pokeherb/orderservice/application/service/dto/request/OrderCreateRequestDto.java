package org.pokeherb.orderservice.application.service.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreateRequestDto(
        UUID productId,
        int quantity,
        UUID orderUserId,
        String productName,
        LocalDateTime dueAt,
        String requestMemo,
        Long startHubId,
        Long endHubId,
        UUID requestVendorId,
        UUID receiveVendorId
) { }


