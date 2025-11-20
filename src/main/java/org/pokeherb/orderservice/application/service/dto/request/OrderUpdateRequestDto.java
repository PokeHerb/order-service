package org.pokeherb.orderservice.application.service.dto.request;

import java.time.LocalDateTime;

public record OrderUpdateRequestDto(
        String productName,
        Integer quantity,
        String requestMemo,
        LocalDateTime dueAt
) { }