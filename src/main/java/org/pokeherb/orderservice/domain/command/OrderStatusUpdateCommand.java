package org.pokeherb.orderservice.domain.command;

import org.pokeherb.orderservice.domain.entity.OrderStatus;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;

import java.time.LocalDateTime;

public record OrderStatusUpdateCommand (
        OrderStatus newStatus,
        LocalDateTime changedAt
){
    public OrderStatusUpdateCommand {
        if(newStatus == null) throw new CustomException(OrderErrorCode.INVALID_ORDER_STATUS);
    }
}
