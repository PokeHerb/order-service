package org.pokeherb.orderservice.application.command;

import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderBasicResponseDto;

import java.util.UUID;

public interface OrderCommandService {
    OrderBasicResponseDto createOrder(OrderCreateRequestDto request);

    OrderBasicResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto request);
}
