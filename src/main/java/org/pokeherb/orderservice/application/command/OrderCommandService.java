package org.pokeherb.orderservice.application.command;

import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderBasicResponseDto;

public interface OrderCommandService {
    OrderBasicResponseDto createOrder(OrderCreateRequestDto request);
}
