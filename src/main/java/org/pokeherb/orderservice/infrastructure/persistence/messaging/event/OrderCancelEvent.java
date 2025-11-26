package org.pokeherb.orderservice.infrastructure.persistence.messaging.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.dto.request.OrderCancelRequestDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.OrderCancelMessageDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler.AbstractOrderEventHandler;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("order.canceled.order")
public class OrderCancelEvent extends AbstractOrderEventHandler {

    @Autowired
    private RabbitProducer rabbitProducer;

    private final OrderCommandService orderCommandService;

    public OrderCancelEvent(
            ObjectMapper objectMapper,
            OrderCommandService orderCommandService
    ) {
        super(objectMapper);
        this.orderCommandService = orderCommandService;
    }

    public void handle(String payload) {
        OrderCancelMessageDto event = readPayload(payload, OrderCancelMessageDto.class);
        OrderCancelRequestDto dto = new OrderCancelRequestDto(event.cancellerId());
        orderCommandService.cancelOrder(event.orderId(), dto);
        log.info("Order canceled via MQ event: orderId={}, cancellerId={}",
                event.orderId(), event.cancellerId());
    }
}