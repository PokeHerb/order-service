package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.domain.Order;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDeleteService {

    private final OrderRepository orderRepository;

    @Transactional
    public void deleteOrder(UUID orderId, String deleterId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));
        order.delete(deleterId, LocalDateTime.now());
    }
}
