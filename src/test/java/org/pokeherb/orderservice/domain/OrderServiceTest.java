package org.pokeherb.orderservice.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.service.*;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.domain.entity.Order;
import org.pokeherb.orderservice.domain.entity.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    private OrderUpdateService orderUpdateService;

    @Autowired
    private OrderCancelService orderCancelService;

    @Autowired
    private OrderDeleteService orderDeleteService;

    @Autowired
    private OrderRepository orderRepository;

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
    @Autowired
    private OrderGetService orderGetService;
    @Autowired
    private OrderSearchService orderSearchService;

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

    private OrderCreateRequestDto createCommand() {
        // 네가 record로 정의했다고 가정 (필드 10개짜리)
        return new OrderCreateRequestDto (
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
        OrderCreateResponseDto response = orderCommandService.createOrder(createCommand());
        UUID orderId = response.getOrderId();

        Order order = orderRepository.findById(orderId).orElseThrow();

        assertEquals(productName, order.getProductName());
        assertEquals(quantity, order.getQuantity());
        assertEquals(requestMemo, order.getRequestMemo());
        assertEquals(dueAt, order.getDueAt());
        assertEquals(OrderStatus.CREATED, order.getOrderStatus());
    }

//    @Test
//    @Transactional
//    @DisplayName("서비스: 주문 정보 수정")
//    @WithMockUser
//    void updateOrder() {
//        OrderResponse created = orderCreateService.createOrder(createCommand());
//        UUID orderId = created.id();
//
//        OrderUpdateCommand command = new OrderUpdateCommand(
//                "(수정)" + productName,
//        quantity + 2,
//    "(수정)" + requestMemo,
//                dueAt.plusHours(1)
//        );
//
//        orderUpdateService.updateOrder(orderId, command);
//        Order order = orderRepository.findById(orderId).orElseThrow();
//        assertTrue(order.getProductName().startsWith("(수정"));
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("서비스: 주문 취소")
//    void cancelOrder() {
//        OrderResponse created = orderCreateService.createOrder(createCommand());
//        UUID orderId = created.id();
//        UUID cancellerId = UUID.randomUUID();
//        orderCancelService.cancelOrder(orderId, cancellerId);
//
//        Order order =  orderRepository.findById(orderId).orElseThrow();
//        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
//        assertEquals(cancellerId, order.getCancelledBy());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("서비스: 주문삭제")
//    void deleteOrder() {
//        OrderResponse created = orderCreateService.createOrder(createCommand());
//        UUID orderId = created.id();
//
//        UUID cancellerId = UUID.randomUUID();
//        orderCancelService.cancelOrder(orderId, cancellerId);
//
//        String deleterId = "test-deleter";
//        orderDeleteService.deleteOrder(orderId, deleterId);
//
//        Order order =  orderRepository.findById(orderId).orElseThrow();
//        assertNotNull(order.getDeletedAt());
//        assertEquals(deleterId, order.getDeletedBy());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("서비스 : 주문 상세 조회")
//    void getOrderDetail(){
//        OrderResponse created = orderCreateService.createOrder(
//                createCommandWithCustomValues(orderUserId, "상세 조회용 상품")
//        );
//        System.out.println("created = " + created);
//
//        OrderResponse detail = orderGetService.getOrder(created.id());
//        System.out.println("detail = " + detail);
//
//        assertEquals(created.id(), detail.id());
//        assertEquals("상세 조회용 상품", detail.productName());
//        assertEquals(orderUserId, detail.orderUserId());
//        System.out.println("orderUserId = " + detail.orderUserId() + "productName = " + detail.productName());
//    }
//
//    @Test
//    @Transactional
//    @DisplayName("서비스 : 주문 검색(orderUserId 기준)")
//    void searchOrderByOrderUserId() {
//        UUID userA = orderUserId;
//        UUID userB = UUID.randomUUID();
//
//        System.out.println("\n=== 주문 데이터 생성 ===");
//        orderCreateService.createOrder(createCommandWithCustomValues(userA, "사용자A-상품1"));
//        orderCreateService.createOrder(createCommandWithCustomValues(userA, "사용자A-상품2"));
//        orderCreateService.createOrder(createCommandWithCustomValues(userB, "사용자B-상품2"));
//
//        System.out.println("생성 완료. userA = " + userA);
//        System.out.println("생성 완료. userB = " + userB);
//
//        OrderSearchCondition condition = new OrderSearchCondition(
//                userA,
//                null,
//                null,
//                null,
//                null
//        );
//
//        System.out.println("\n=== 검색 조건 ===");
//        System.out.println("orderUserId = " + condition.orderUserId());
//
//        Page<OrderSummaryResponse> result =
//                orderSearchService.searchOrders(condition, PageRequest.of(0, 10));
//
//        System.out.println("\n=== 검색 결과 ===");
//        result.getContent().forEach(o -> {
//            System.out.println("Order ID = " + o.id()
//                    + ", userId = " + o.orderUserId()
//                    + ", productName = " + o.productName());
//        });
//
//        assertEquals(2, result.getTotalElements());
//        assertTrue(result.getContent().stream()
//                .allMatch(o -> o.orderUserId().equals(userA)));
//
//        System.out.println("\n=== orderUserId 검색 테스트 완료 ===\n");
//    }
//
//
//    @Test
//    @Transactional
//    @DisplayName("서비스: 주문 검색 (productName 부분 검색)")
//    void searchOrdersByProductName() {
//
//        System.out.println("\n=== 주문 데이터 생성 ===");
//        orderCreateService.createOrder(createCommandWithCustomValues(orderUserId, "매운닭발"));
//        orderCreateService.createOrder(createCommandWithCustomValues(orderUserId, "순한닭발"));
//        orderCreateService.createOrder(createCommandWithCustomValues(orderUserId, "치즈돈까스"));
//
//        System.out.println("생성된 상품명: 매운닭발 / 순한닭발 / 치즈돈까스");
//
//        OrderSearchCondition condition = new OrderSearchCondition(
//                null,
//                null,
//                null,
//                "닭발",
//                null
//        );
//
//        System.out.println("\n=== 검색 조건 ===");
//        System.out.println("productName contains = " + condition.productName());
//
//        Page<OrderSummaryResponse> result =
//                orderSearchService.searchOrders(condition, PageRequest.of(0, 10));
//
//        System.out.println("\n=== 검색 결과 ===");
//        result.getContent().forEach(o -> {
//            System.out.println("Order ID = " + o.id()
//                    + ", productName = " + o.productName());
//        });
//
//        assertEquals(2, result.getTotalElements());
//        assertTrue(result.getContent().stream()
//                .allMatch(o -> o.productName().contains("닭발")));
//
//        System.out.println("\n=== productName 검색 테스트 완료 ===\n");
//    }
}
