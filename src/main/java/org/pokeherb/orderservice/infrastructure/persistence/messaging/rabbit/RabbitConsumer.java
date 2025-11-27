package org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler.OrderEventHandler;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitConsumer {

    private final Map<String, OrderEventHandler> handlers;

    @RabbitListener(queues = "order")
    public void listen(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);

        log.info("Received MQ message - routingKey={}, payload={}", routingKey, payload);

        OrderEventHandler handler = handlers.get(routingKey);
        if (handler == null) {
            log.warn("Unhandled routingKey: {}", routingKey);
            return;
        }

        handler.handle(payload);
    }
}