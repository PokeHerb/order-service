package org.pokeherb.orderservice.application.service;


import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.OrderCreateCommand;
import org.pokeherb.orderservice.domain.Order;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.presentation.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCreateService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(OrderCreateCommand command) {
        Order order = Order.create(
                command.productId(),
                command.quantity(),
                command.orderUserId(),
                command.productName(),
                command.dueAt(),
                command.requestMemo(),
                command.startHubId(),
                command.endHubId(),
                command.requestVendorId(),
                command.receiveVendorId()
        );

        Order saved = orderRepository.save(order);
        return OrderResponse.from(saved);
    }
}

