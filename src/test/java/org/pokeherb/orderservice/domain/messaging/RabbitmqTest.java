package org.pokeherb.orderservice.domain.messaging;

import org.junit.jupiter.api.Test;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.infrastructure.persistence.messaging.rabbit.RabbitProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
class RabbitmqTest {

    @Autowired
    private RabbitProducer rabbitProducer;

    @MockitoBean
    private OrderCommandService orderCommandService;

    @Test
    void OrderCreatedEventTest() throws Exception {
        UUID productId = UUID.fromString("7f3c0e33-4c71-4c1e-86b7-2ba3cb52a9c4");
        int quantity = 1;
        UUID orderUserId =  UUID.randomUUID();
        LocalDateTime dueAt =  LocalDateTime.now().plusMinutes(100);
        String requestMemo = "1234";
        String productName = "test product";
        Long startHubId = 1L;
        Long endHubId = 4L;
        UUID requestVendorId =  UUID.randomUUID();
        UUID receiveVendorId  = UUID.randomUUID();
        String vendorAddress = "부산 기장군 기장읍 동부산관광로 42";
        UUID receiverSlackId = UUID.randomUUID();
        String receiverName = "testName";

        OrderCreateRequestDto dto = new OrderCreateRequestDto(
                productId,
                quantity,
                orderUserId,
                productName,
                dueAt,
                requestMemo,
                startHubId,
                endHubId,
                requestVendorId,
                receiveVendorId,
                vendorAddress,
                receiverSlackId,
                receiverName
        );
        rabbitProducer.publishDeliveryEvent(dto, "order.created");
    }

}