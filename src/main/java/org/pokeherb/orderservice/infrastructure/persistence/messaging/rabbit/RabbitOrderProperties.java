package org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbit.order")
public record RabbitOrderProperties(
        String exchange,
        String queue,
        String routingKey
) {
}