package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.OrderUpdateCommand;
import org.pokeherb.orderservice.domain.Order;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.exception.OrderNotFoundException;
import org.pokeherb.orderservice.presentation.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderUpdateService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse updateOrder(UUID orderId, OrderUpdateCommand command){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.updateOrderInfo(
                command.productName(),
                command.quantity(),
                command.requestMemo(),
                command.dueAt()
        );
        return  OrderResponse.from(order);
    }
}
