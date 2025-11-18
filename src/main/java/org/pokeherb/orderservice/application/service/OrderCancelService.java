package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.domain.Order;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.exception.OrderNotFoundException;
import org.pokeherb.orderservice.presentation.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCancelService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse cancelOrder(UUID orderId, UUID cancellerId){
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new OrderNotFoundException(orderId));
        order.cancelOrder(cancellerId, LocalDateTime.now());

        return OrderResponse.from(order);
    }
}
