package org.pokeherb.orderservice.infrastructure.persistence.messaging.event;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.dto.request.OrderCancelRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponseDto;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.HubOrderCanceledMessageDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.OrderCancelMessageDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler.AbstractOrderEventHandler;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit.RabbitProducer;
import org.springframework.stereotype.Component;

@Slf4j
@Component("hub.canceled.order")
public class OrderCancelEvent extends AbstractOrderEventHandler {

    private final OrderCommandService orderCommandService;

    private final RabbitProducer rabbitProducer;

    public OrderCancelEvent(
            ObjectMapper objectMapper,
            OrderCommandService orderCommandService,
            RabbitProducer rabbitProducer
    ) {
        super(objectMapper);
        this.orderCommandService = orderCommandService;
        this.rabbitProducer = rabbitProducer;
    }

    public void handle(String payload) {
        try {
            OrderCancelMessageDto event = readPayload(payload, OrderCancelMessageDto.class);
            OrderCancelRequestDto dto = new OrderCancelRequestDto(event.cancellerId());
            OrderResponseDto response = orderCommandService.cancelOrder(event.orderId(), dto);
            log.info("Order canceled via MQ event: orderId={}, cancellerId={}",
                    event.orderId(), event.cancellerId());
            HubOrderCanceledMessageDto hubEvent = new HubOrderCanceledMessageDto(
                    event.orderId(),
                    response.productName(),
                    response.quantity()
            );
            rabbitProducer.publishDeliveryEvent(hubEvent, "hub.canceled.order");

        } catch (CustomException e) {
            log.error("Error processing OrderCancelEvent", e);
        } catch (Exception e) {
            log.error("Unexpected error while processing order create MQ. payload={}", payload, e);
        }
    }
}