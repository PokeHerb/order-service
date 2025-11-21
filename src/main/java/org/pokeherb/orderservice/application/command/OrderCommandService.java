package org.pokeherb.orderservice.application.command;

import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderStatusUpdateMessageDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponseDto;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;

import java.util.UUID;

public interface OrderCommandService {
    OrderCreateResponseDto createOrder(OrderCreateRequestDto request);

    OrderCreateResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto request);

    OrderResponseDto cancelOrder(UUID orderId, UUID cancellerId);

    void deleteOrder(UUID orderId, String username);

    void applyStatusUpdate(OrderStatusUpdateMessageDto messageDto);
}
