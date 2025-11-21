package org.pokeherb.orderservice.domain.command;

import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderUpdateCommand(
        String productName,
        Integer quantity,
        String requestMemo,
        LocalDateTime dueAt
){
//    public OrderUpdateCommand {
//    }
}
