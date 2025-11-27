package org.pokeherb.orderservice.infrastructure.persistence.messaging.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.application.service.OrderStatusUpdateService;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.OrderStatusUpdateMessageDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler.AbstractOrderEventHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("order.status")
public class OrderStatusUpdateEvent extends AbstractOrderEventHandler {

    private final OrderStatusUpdateService orderStatusUpdateService;

    public OrderStatusUpdateEvent(
            ObjectMapper objectMapper,
            OrderStatusUpdateService orderStatusUpdateService) {
        super(objectMapper);
        this.orderStatusUpdateService = orderStatusUpdateService;
    }

    public void handle(String payload) {
        try {
            OrderStatusUpdateMessageDto event = readPayload(payload, OrderStatusUpdateMessageDto.class);
            orderStatusUpdateService.applyStatusUpdate(event);
            
        } catch (CustomException e) {
            log.error("Error processing OrderStatusUpdateEvent", e);
        } catch (Exception e) {
            log.error("Unexpected error while processing order create MQ. payload={}", payload, e);
        }
    }
}
