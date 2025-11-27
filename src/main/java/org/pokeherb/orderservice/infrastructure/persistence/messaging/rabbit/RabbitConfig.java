package org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitOrderProperties.class)
public class RabbitConfig {
    private final RabbitOrderProperties orderProperties;

    @Bean
    public TopicExchange deliveryExchange() {
        return new TopicExchange(orderProperties.exchange(), true, false);
    }

    @Bean
    public Queue deliveryQueue() {
        return QueueBuilder.durable(orderProperties.queue()).build();
    }

    @Bean
    public Binding deliveryBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange)
                .with(orderProperties.routingKey());
    }
}