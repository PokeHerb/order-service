package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.presentation.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderGetService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        return OrderResponse.from(order);
    }
}