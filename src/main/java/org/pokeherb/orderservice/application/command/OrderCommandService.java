package org.pokeherb.orderservice.application.command;

import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;

import java.util.UUID;

public interface OrderCommandService {
    OrderCreateResponseDto createOrder(OrderCreateRequestDto request);

    OrderCreateResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto request);
}
