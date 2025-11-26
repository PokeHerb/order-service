package org.pokeherb.orderservice.application.service.dto.request;

import java.util.UUID;

public record OrderCancelRequestDto(
        UUID cancellerId
) {}
