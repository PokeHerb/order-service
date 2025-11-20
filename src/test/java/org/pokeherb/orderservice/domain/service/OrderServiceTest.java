package org.pokeherb.orderservice.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.*;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderStatusUpdateMessageDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponse;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponse;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.entity.OrderStatus;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {

    @Autowired
    private OrderCommandService orderCommandService;

    @Autowired
    private OrderCancelService orderCancelService;

    @Autowired
    private OrderDeleteService orderDeleteService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderGetService orderGetService;

    @Autowired
    private OrderSearchService orderSearchService;

    @Autowired
    private OrderStatusUpdateService orderStatusUpdateService;

    private UUID productId;
    private UUID orderUserId;
    private Long startHubId;
    private Long endHubId;
    private UUID requestVendorId;
    private UUID receiveVendorId;
    private String productName;
    private int quantity;
    private String requestMemo;
    private LocalDateTime dueAt;

    @BeforeEach
    void init() {
        productId = UUID.randomUUID();
        orderUserId = UUID.randomUUID();
        startHubId = UUID.randomUUID().getMostSignificantBits();
        endHubId = UUID.randomUUID().getMostSignificantBits();
        requestVendorId = UUID.randomUUID();
        receiveVendorId = UUID.randomUUID();

        productName = "테스트 상품";
        quantity = 3;
        requestMemo = "테스트 메모";
        dueAt = LocalDateTime.now().plusHours(2);
    }

    private OrderCreateRequestDto createRequest() {
        return new OrderCreateRequestDto(
                productId,
                quantity,
                orderUserId,
                productName,
                dueAt,
                requestMemo,
                startHubId,
                endHubId,
                requestVendorId,
                receiveVendorId
        );
    }

    // userId, productName 커스터마이징해서 쓰고 싶을 때
    private OrderCreateRequestDto createCommandWithCustomValues(UUID userId, String customName) {
        return new OrderCreateRequestDto(
                productId,
                quantity,
                userId,
                customName,
                dueAt,
                requestMemo,
                startHubId,
                endHubId,
                requestVendorId,
                receiveVendorId
        );
    }

    @Test
    @Transactional
    @DisplayName("서비스: 주문 생성")
    @WithMockUser(username = "test-user", roles = "USER")
    void createOrder(){
        OrderCreateResponseDto response = orderCommandService.createOrder(createRequest());
        UUID orderId = response.getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow();

        assertEquals(productName, order.getProductName());
        assertEquals(quantity, order.getQuantity());
        assertEquals(requestMemo, order.getRequestMemo());
        assertEquals(dueAt, order.getDueAt());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

    @Test
    @Transactional
    @DisplayName("주문 수정: 일부 필드만 수정(부분 업데이트)")
    @WithMockUser
    void updateOrder() {
        OrderCreateResponseDto created = orderCommandService.createOrder(createRequest());
        UUID orderId = created.getOrderId();

        OrderUpdateRequestDto updateRequest = new OrderUpdateRequestDto(
                "(수정)" + productName,
                null,                  // quantity 수정 안 함
                null,                  // requestMemo 수정 안 함
                null                   // dueAt 수정 안 함
        );

        OrderCreateResponseDto updated = orderCommandService.updateOrder(orderId, updateRequest);

        Order order = orderRepository.findById(orderId).orElseThrow();

        assertTrue(order.getProductName().startsWith("(수정"));
        assertEquals(quantity, order.getQuantity());
        assertEquals(requestMemo, order.getRequestMemo());
        assertEquals(dueAt, order.getDueAt());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

    @Test
    @Transactional
    @DisplayName("서비스: 주문 취소")
    void cancelOrder() {
        OrderCreateResponseDto created = orderCommandService.createOrder(createRequest());
        UUID orderId = created.getOrderId();

        UUID cancellerId = UUID.randomUUID();
        OrderResponse response = orderCancelService.cancelOrder(orderId, cancellerId);

        Order order =  orderRepository.findById(orderId).orElseThrow();
        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        assertEquals(cancellerId, order.getCancelledBy());
        assertNotNull(order.getCancelledAt());
        assertEquals(OrderStatus.CANCELLED, response.status());
    }

    @Test
    @Transactional
    @DisplayName("서비스: 주문삭제")
    void deleteOrder() {
        OrderCreateResponseDto created = orderCommandService.createOrder(createRequest());
        UUID orderId = created.getOrderId();

        String deleterId = "test-deleter";
        orderDeleteService.deleteOrder(orderId, deleterId);

        Order order =  orderRepository.findById(orderId).orElseThrow();
        assertNotNull(order.getDeletedAt());
        assertEquals(deleterId, order.getDeletedBy());
    }

    @Test
    @Transactional
    @DisplayName("서비스 : 주문 상세 조회")
    void getOrderDetail(){
        OrderCreateResponseDto created = orderCommandService.createOrder(createRequest());
        UUID orderId = created.getOrderId();

        OrderResponse response = orderGetService.getOrder(orderId);

        assertEquals(orderId, response.id());
        assertEquals(productName, response.productName());
        assertEquals(orderUserId, response.orderUserId());
    }
    @Test
    @Transactional
    @DisplayName("상태 변경: CREATED -> ASSIGNED (MQ 이벤트 기반)")
    void updateStatus_assigned_success(){
        OrderCreateResponseDto created = orderCommandService.createOrder(createRequest());
        UUID orderId = created.getOrderId();
        UUID driverId = UUID.randomUUID();

        OrderStatusUpdateMessageDto message = new OrderStatusUpdateMessageDto(
                orderId,
                "ASSIGNED",
                driverId,
                "COURIER",
                LocalDateTime.now()
        );

        orderStatusUpdateService.applyStatusUpdate(message);

        Order order = orderRepository.findById(orderId).orElseThrow();
        assertEquals(OrderStatus.ASSIGNED, order.getOrderStatus());
        assertEquals(driverId, order.getDeliveryDriverId());
    }

    @Test
    @Transactional
    @DisplayName("상태 변경: 잘못된 전이는 예외 발생 ex) CREATED -> COMPLETED")
    void updateStatus_invalidTransition() {
        OrderCreateResponseDto created = orderCommandService.createOrder(createRequest());
        UUID orderId = created.getOrderId();

        OrderStatusUpdateMessageDto message = new OrderStatusUpdateMessageDto(
                orderId,
                "COMPLETED",
                null,
                "COURIER",
                LocalDateTime.now()
        );

        CustomException ex = assertThrows(CustomException.class, () -> orderStatusUpdateService.applyStatusUpdate(message));

        assertEquals(OrderErrorCode.INVALID_STATUS_TRANSITION, ex.getCode());
    }
    @Test
    @Transactional
    @DisplayName("서비스 : 주문 검색(orderUserId 기준)")
    void searchOrderByOrderUserId() {
        UUID userA = orderUserId;
        UUID userB = UUID.randomUUID();

        System.out.println("\n=== 주문 데이터 생성 ===");
        orderCommandService.createOrder(createCommandWithCustomValues(userA, "사용자A-상품1"));
        orderCommandService.createOrder(createCommandWithCustomValues(userA, "사용자A-상품2"));
        orderCommandService.createOrder(createCommandWithCustomValues(userB, "사용자B-상품1"));


        OrderSearchConditionRequestDto condition = new OrderSearchConditionRequestDto(
                userA,
                null,
                null,
                null,
                null
        );


        Page<OrderSummaryResponse> result =
                orderSearchService.searchOrders(condition, PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .allMatch(o -> o.orderUserId().equals(userA)));
    }


    @Test
    @Transactional
    @DisplayName("서비스: 주문 검색 (productName 부분 검색)")
    void searchOrdersByProductName() {
        // given
        orderCommandService.createOrder(createCommandWithCustomValues(orderUserId, "매운닭발"));
        orderCommandService.createOrder(createCommandWithCustomValues(orderUserId, "순한닭발"));
        orderCommandService.createOrder(createCommandWithCustomValues(orderUserId, "치즈돈까스"));

        OrderSearchConditionRequestDto condition = new OrderSearchConditionRequestDto(
                null,      // orderUserId
                null,      // requestVendorId
                null,      // productId
                "닭발",    // productName contains
                null       // status
        );

        // when
        Page<OrderSummaryResponse> result =
                orderSearchService.searchOrders(condition, PageRequest.of(0, 10));

        // then
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .allMatch(o -> o.productName().contains("닭발")));
    }

    @Test
    @Transactional
    @DisplayName("서비스: 주문 목록 페이지네이션")
    void searchOrdersWithPagination() {
        // given
        UUID user = orderUserId;

        // 25개 주문 생성
        for (int i = 1; i <= 25; i++) {
            orderCommandService.createOrder(
                    createCommandWithCustomValues(user, "상품-" + i)
            );
        }

        OrderSearchConditionRequestDto condition = new OrderSearchConditionRequestDto(
                user,
                null,
                null,
                null,
                null
        );

        // when: 첫 페이지 (0, size=10)
        PageRequest page0 = PageRequest.of(0, 10);
        Page<OrderSummaryResponse> pageResult0 =
                orderSearchService.searchOrders(condition, page0);

        // then
        assertEquals(25, pageResult0.getTotalElements());
        assertEquals(10, pageResult0.getContent().size());
        assertEquals(0, pageResult0.getNumber());
        assertEquals(3, pageResult0.getTotalPages()); // 25 / 10 -> 3페이지

        // when: 두 번째 페이지 (1, size=10)
        PageRequest page1 = PageRequest.of(1, 10);
        Page<OrderSummaryResponse> pageResult1 =
                orderSearchService.searchOrders(condition, page1);

        assertEquals(10, pageResult1.getContent().size());
        assertEquals(1, pageResult1.getNumber());

        // when: 세 번째 페이지 (2, size=10)
        PageRequest page2 = PageRequest.of(2, 10);
        Page<OrderSummaryResponse> pageResult2 =
                orderSearchService.searchOrders(condition, page2);

        assertEquals(5, pageResult2.getContent().size());  // 마지막 페이지는 5개
        assertEquals(2, pageResult2.getNumber());
    }

}
