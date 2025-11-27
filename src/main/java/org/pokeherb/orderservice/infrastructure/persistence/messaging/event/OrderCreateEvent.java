package org.pokeherb.orderservice.infrastructure.persistence.messaging.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.DeliveryCreateEvent;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.dto.DeliveryCreateMessageDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.messageHandler.AbstractOrderEventHandler;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit.RabbitProducer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component("order.created")
public class OrderCreateEvent extends AbstractOrderEventHandler {

    private final OrderCommandService orderCommandService;
    private final RabbitProducer rabbitProducer;

    public OrderCreateEvent(
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
            // 1. MQ에서 주문 생성 요청 수신
            OrderCreateRequestDto request =
                    readPayload(payload, OrderCreateRequestDto.class);

            // 2. 주문 생성 (DB 저장 완료 후 orderId 반환)
            OrderCreateResponseDto response = orderCommandService.createOrder(request);

            UUID orderId = response.getOrderId();
            LocalDateTime createdAt = response.getCreatedAt();

            // 3. 주문 생성 결과(orderId) + 기존 요청 값들을 합쳐서 배송 이벤트 생성
            DeliveryCreateEvent deliveryEvent = new DeliveryCreateEvent(
                    response.getOrderId(),
                    request.productId(),
                    request.quantity(),
                    request.orderUserId(),
                    request.productName(),
                    request.dueAt(),
                    request.requestMemo(),
                    request.startHubId(),
                    request.endHubId(),
                    request.requestVendorId(),
                    request.receiveVendorId(),
                    request.vendorAddress(),
                    request.receiverSlackId(),
                    request.receiverName(),
                    createdAt
            );
            // 4. 배송 서비스로는 "껍데기 생성"용 최소 이벤트만 발행 (orderId만)
            DeliveryCreateMessageDto deliveryCreateMessage =
                    new DeliveryCreateMessageDto(orderId);
            rabbitProducer.publishDeliveryEvent(deliveryCreateMessage, "delivery.create");

            // 5. 허브 쪽으로 이벤트 발행
            rabbitProducer.publishDeliveryEvent(deliveryEvent, "hub.created.order");

            // 6. 로그
            log.info("Order created from MQ and send hub. orderId={}, productId={}, userId={}",
                    response.getOrderId(), request.productId(), request.orderUserId());

        } catch (CustomException e) {
            log.warn("Failed to create order from MQ. payload={}, reason={}",
                    payload, e.getMessage());

        } catch (Exception e) {
            log.error("Unexpected error while processing order create MQ. payload={}", payload, e);
            throw e;
        }
    }

}