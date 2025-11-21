package org.pokeherb.orderservice.application.query;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponse;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.domain.repository.OrderQueryRepository;
import org.pokeherb.orderservice.domain.repository.OrderRepository;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderQueryServiceImpl implements OrderQueryService {

    private final OrderRepository orderRepository;

    private final OrderQueryRepository orderQueryDao;

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        return OrderResponseDto.from(order);
    }

    @Transactional
    public Page<OrderSummaryResponse> searchOrders(OrderSearchConditionRequestDto condition, Pageable pageable) {
        return orderQueryDao.search(condition, pageable);
    }
}
