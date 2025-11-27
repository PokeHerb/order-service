package org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler;

public interface OrderEventHandler {
    void handle(String payload);
}