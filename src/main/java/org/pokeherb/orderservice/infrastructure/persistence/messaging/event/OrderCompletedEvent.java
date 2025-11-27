package org.pokeherb.orderservice.infrastructure.persistence.messaging.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler.AbstractOrderEventHandler;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component("order.complete")
public class OrderCompletedEvent extends AbstractOrderEventHandler {

    @Autowired
    private RabbitProducer rabbitProducer;

    private final OrderCommandService orderCommandService;

    public OrderCompletedEvent(
            ObjectMapper objectMapper,
            OrderCommandService orderCommandService,
            RabbitProducer rabbitProducer

    ) {
        super(objectMapper);
        this.orderCommandService = orderCommandService;
        this.rabbitProducer = rabbitProducer;
    }
    public void handle(String payload) {
        OrderCreateRequestDto event =
                readPayload(payload, OrderCreateRequestDto.class);

        OrderCreateResponseDto response = orderCommandService.createOrder(event);
        rabbitProducer.publishDeliveryEvent(event, "hub.created.order");
        log.info(
                "Order created and forwarded to other domain. orderId={}, productId={}, userId={}",
                response.getOrderId(),
                event.productId(),
                event.orderUserId()
        );
    }
}