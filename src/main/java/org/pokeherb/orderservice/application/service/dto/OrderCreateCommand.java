package org.pokeherb.orderservice.application.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreateCommand(
        UUID productId,
        int quantity,
        UUID orderUserId,
        String productName,
        LocalDateTime dueAt,
        String requestMemo,
        UUID startHubId,
        UUID endHubId,
        UUID requestVendorId,
        UUID receiveVendorId
) { }


