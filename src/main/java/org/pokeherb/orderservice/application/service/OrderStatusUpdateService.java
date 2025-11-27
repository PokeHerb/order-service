package org.pokeherb.orderservice.application.service;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.domain.command.OrderStatusUpdateCommand;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.entity.OrderStatus;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.domain.repository.OrderRepository;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.OrderStatusUpdateMessageDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderStatusUpdateService {

    private final OrderRepository orderRepository;

    @Transactional
    public void applyStatusUpdate(OrderStatusUpdateMessageDto messageDto){
        Order order = orderRepository.findById(messageDto.orderId())
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        /*
        *      추후 권한 체크 추가
        */

        OrderStatus newStatus;
        try{
            newStatus = OrderStatus.valueOf(messageDto.orderStatus());
        } catch (IllegalArgumentException e){
            throw new CustomException(OrderErrorCode.INVALID_ORDER_STATUS);
        }
        OrderStatusUpdateCommand command = new OrderStatusUpdateCommand(
                newStatus,
                messageDto.changedAt()
        );
        order.applyStatusUpdate(command);
    }
}
