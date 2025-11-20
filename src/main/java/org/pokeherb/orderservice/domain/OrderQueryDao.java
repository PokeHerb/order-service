package org.pokeherb.orderservice.domain;

import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.presentation.dto.OrderSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryDao {
    Page<OrderSummaryResponse> search(OrderSearchConditionRequestDto condition, Pageable pageable);
}
