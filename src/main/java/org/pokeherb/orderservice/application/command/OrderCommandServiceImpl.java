package org.pokeherb.orderservice.application.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderBasicResponseDto;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.command.OrderCreateCommand;
import org.pokeherb.orderservice.domain.command.OrderUpdateCommand;
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
        // 1. 입력 DTO 검증 (형식/널체크 등)
        validateOrderCreate(request);

        // 2. DTO -> 도메인 커맨드 변환
        OrderCreateCommand command = toCommand(request);

        // 3. 도메인에 "주문 생성" 책임 위임
        Order order = Order.create(command);

        // 4. 저장 및 응답 DTO 변환
        Order saved = orderRepository.save(order);

        return OrderBasicResponseDto.from(saved);
    }

    @Transactional
    public OrderBasicResponseDto updateOrder(OrderUpdateRequestDto request) {
        OrderUpdateCommand updateCommand = toCommand(request);

    }

    private void validateOrderCreate(OrderCreateRequestDto request) {
        if (request.productId() == null) {
            throw new CustomException(OrderErrorCode.INVALID_PRODUCT);
        }
        if (request.orderUserId() == null) {
            throw new CustomException(OrderErrorCode.INVALID_ORDER_USER);
        }
    }

    // DTO -> Domain Command 변환 메서드
    private OrderCreateCommand toCommand(OrderCreateRequestDto request) {
        return new OrderCreateCommand(
                request.productId(),
                request.quantity(),
                request.orderUserId(),
                request.productName(),
                request.dueAt(),
                request.requestMemo(),
                request.startHubId(),
                request.endHubId(),
                request.requestVendorId(),
                request.receiveVendorId()
        );
    }
}