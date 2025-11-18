package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.OrderSearchCondition;
import org.pokeherb.orderservice.infrastructure.persistence.OrderQueryDao;
import org.pokeherb.orderservice.presentation.dto.OrderSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderSearchService {

    private final OrderQueryDao orderQueryDao;

    @Transactional
    public Page<OrderSummaryResponse> searchOrders(OrderSearchCondition condition, Pageable pageable) {
        return orderQueryDao.search(condition, pageable);
    }
}
