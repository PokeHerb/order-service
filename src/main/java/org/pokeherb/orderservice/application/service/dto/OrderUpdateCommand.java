package org.pokeherb.orderservice.application.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderUpdateCommand(
        String productName,
        int quantity,
        String requestMemo,
        LocalDateTime dueAt
) { }
