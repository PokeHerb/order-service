package org.pokeherb.orderservice.domain.repository;

import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepository {
    Page<OrderSummaryResponseDto> search(OrderSearchConditionRequestDto condition, Pageable pageable);
}
