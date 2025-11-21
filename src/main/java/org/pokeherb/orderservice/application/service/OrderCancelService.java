package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.repository.OrderRepository;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponse;
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
        if (cancellerId == null) {
            // 필요하면 OrderErrorCode에 CANCEL_USER_INVALID 같은 코드 추가
            throw new CustomException(OrderErrorCode.INVALID_CANCEL_USER);
        }

        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        order.cancelOrder(cancellerId, LocalDateTime.now());

        return OrderResponse.from(order);
    }
}
