package org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.infrastructure.persistence.exception.RabbitErrorCode;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractOrderEventHandler implements OrderEventHandler {

    protected final ObjectMapper objectMapper;

    protected <T> T readPayload(String payload, Class<T> type) {
        try {
            return objectMapper.readValue(payload, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse event payload: {}", payload, e);
            throw new CustomException(RabbitErrorCode.RABBIT_JSON_PROCESSING_FAILED);
        }
    }
}