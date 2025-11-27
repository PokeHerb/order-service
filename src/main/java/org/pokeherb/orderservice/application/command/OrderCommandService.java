package org.pokeherb.orderservice.application.command;

import org.pokeherb.orderservice.application.service.dto.request.OrderCancelRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponseDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.OrderStatusUpdateMessageDto;

import java.util.UUID;

public interface OrderCommandService {
    OrderCreateResponseDto createOrder(OrderCreateRequestDto request);

    OrderCreateResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto dto);

    OrderResponseDto cancelOrder(UUID orderId, OrderCancelRequestDto request);

    void deleteOrder(UUID orderId, String username);

    void applyStatusUpdate(OrderStatusUpdateMessageDto messageDto);
}
