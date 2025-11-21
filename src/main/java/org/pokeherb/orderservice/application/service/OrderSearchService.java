package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.domain.repository.OrderQueryRepository;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderSearchService {

    private final OrderQueryRepository orderQueryDao;

    @Transactional
    public Page<OrderSummaryResponse> searchOrders(OrderSearchConditionRequestDto condition, Pageable pageable) {
        return orderQueryDao.search(condition, pageable);
    }
}
