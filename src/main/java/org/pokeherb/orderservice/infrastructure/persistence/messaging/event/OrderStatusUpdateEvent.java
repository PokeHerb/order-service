package org.pokeherb.orderservice.infrastructure.persistence.messaging.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.OrderStatusUpdateService;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.OrderStatusUpdateMessageDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler.AbstractOrderEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("order.status")
public class OrderStatusUpdateEvent extends AbstractOrderEventHandler {

    private final OrderCommandService orderCommandService;
    private final OrderStatusUpdateService orderStatusUpdateService;

    public OrderStatusUpdateEvent(
            ObjectMapper objectMapper,
            OrderCommandService orderCommandService,
            OrderStatusUpdateService orderStatusUpdateService) {
        super(objectMapper);
        this.orderCommandService = orderCommandService;
        this.orderStatusUpdateService = orderStatusUpdateService;
    }

    public void handle(String payload) {
        OrderStatusUpdateMessageDto event = readPayload(payload, OrderStatusUpdateMessageDto.class);
        orderStatusUpdateService.applyStatusUpdate(event);
    }
}
