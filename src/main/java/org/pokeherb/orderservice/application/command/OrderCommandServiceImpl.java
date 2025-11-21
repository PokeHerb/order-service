package org.pokeherb.orderservice.application.command;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.domain.repository.OrderRepository;
import org.pokeherb.orderservice.domain.command.OrderCreateCommand;
import org.pokeherb.orderservice.domain.command.OrderUpdateCommand;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;

    // Create
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto request) {
        // 1. 입력 DTO 검증 (형식/널체크 등)
        validateOrderCreate(request);

        // 2. DTO -> 도메인 커맨드 변환
        OrderCreateCommand command = createToCommand(request);

        // 3. 도메인에 "주문 생성" 책임 위임
        Order order = Order.create(command);

        // 4. 저장 및 응답 DTO 변환
        Order saved = orderRepository.save(order);

        return OrderCreateResponseDto.from(saved);
    }

    // Order
    @Transactional
    public OrderCreateResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        OrderUpdateCommand updateCommand = updateToCommand(request);

        order.update(updateCommand);
        return OrderCreateResponseDto.from(order);
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
    private OrderCreateCommand createToCommand(OrderCreateRequestDto request) {
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
    private OrderUpdateCommand updateToCommand(OrderUpdateRequestDto request) {
        return new OrderUpdateCommand(
                request.productName(),
                request.quantity(),
                request.requestMemo(),
                request.dueAt()
        );
    }
}