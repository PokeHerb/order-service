package org.pokeherb.orderservice.infrastructure.persistence;

import org.pokeherb.orderservice.application.service.dto.OrderSearchCondition;
import org.pokeherb.orderservice.presentation.dto.OrderSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryDao {
    Page<OrderSummaryResponse> search(OrderSearchCondition condition, Pageable pageable);
}
