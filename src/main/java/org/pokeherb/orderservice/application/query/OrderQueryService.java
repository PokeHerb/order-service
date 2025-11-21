package org.pokeherb.orderservice.application.query;

import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderQueryService {
    OrderResponseDto getOrder(UUID orderId);

    Page<OrderSummaryResponseDto> searchOrders(OrderSearchConditionRequestDto condition, Pageable pageable);
}
