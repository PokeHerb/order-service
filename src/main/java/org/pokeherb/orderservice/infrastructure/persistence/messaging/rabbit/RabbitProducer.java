package org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.infrastructure.persistence.exception.RabbitErrorCode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RabbitProducer {

    private final RabbitTemplate template;
    private final ObjectMapper objectMapper;
    private final RabbitOrderProperties deliveryProperties;

    public void publishDeliveryEvent(Object payload, String routingKey) {
        sendMessage("pokeherb", routingKey, payload);
    }

    private void sendMessage(String exchange, String routingKey, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            Message message = new Message(json.getBytes(StandardCharsets.UTF_8), messageProperties);

            template.convertAndSend(exchange, routingKey, message);
        } catch (JsonProcessingException e) {
            throw new CustomException(RabbitErrorCode.RABBIT_JSON_PROCESSING_FAILED);
        }
    }
}