package org.pokeherb.orderservice.application.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderBasicResponseDto;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderBasicResponseDto createOrder(OrderCreateRequestDto request) {
        validateOrderCreate(request);

        Order order = Order.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .orderUserId(request.orderUserId())
                .productName(request.productName())
                .dueAt(request.dueAt())
                .requestMemo(request.requestMemo())
                .startHubId(request.startHubId())
                .endHubId(request.endHubId())
                .requestVendorId(request.requestVendorId())
                .receiveVendorId(request.receiveVendorId())
                .build();
        Order saved = orderRepository.save(order);

        return OrderBasicResponseDto.from(saved);
    }

    private void validateOrderCreate(OrderCreateRequestDto request) {
        if (request.productId() == null) {
            throw new CustomException(OrderErrorCode.INVALID_PRODUCT);
        }
        if (request.orderUserId() == null) {
            throw new CustomException(OrderErrorCode.INVALID_ORDER_USER);
        }
        if (request.productName() == null || request.productName().isBlank()) {
            throw new CustomException(OrderErrorCode.INVALID_PRODUCT_NAME);
        }
        if (request.quantity() <= 0) {
            throw new CustomException(OrderErrorCode.INVALID_QUANTITY);
        }
        if (request.dueAt() != null && request.dueAt().isBefore(LocalDateTime.now())) {
            throw new CustomException(OrderErrorCode.INVALID_DUE_DATE);
        }
    }
}