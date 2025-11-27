package org.pokeherb.orderservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.query.OrderQueryService;
import org.pokeherb.orderservice.application.service.dto.request.OrderCancelRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponseDto;
import org.pokeherb.orderservice.domain.entity.OrderStatus;
import org.pokeherb.orderservice.global.infrastructure.CustomResponse;
import org.pokeherb.orderservice.global.infrastructure.success.GeneralSuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/order")
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    // 주문 생성
    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','HUG_MANAGER','DELIVERY_MANAGER','COMPANY_MANAGER')")
    public CustomResponse<OrderCreateResponseDto> createOrder(
            @RequestBody OrderCreateRequestDto request
    ) {
        OrderCreateResponseDto response = orderCommandService.createOrder(request);

        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    // 주문 수정
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER','COMPANY_MANAGER')")
    public CustomResponse<OrderCreateResponseDto> updateOrder(@PathVariable UUID orderId, @RequestBody OrderUpdateRequestDto request){
        OrderCreateResponseDto response = orderCommandService.updateOrder(orderId, request);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    // 주문 취소
    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER','DELIVERY_MANAGER','COMPANY_MANAGER')")
    public CustomResponse<OrderResponseDto> cancelOrder(@PathVariable UUID orderId, @RequestHeader("X-User-Id") UUID cancellerId) {
        OrderCancelRequestDto dto = new OrderCancelRequestDto(cancellerId);
        OrderResponseDto response = orderCommandService.cancelOrder(orderId, dto);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('MASTER','HUB_MANAGER')")
    public CustomResponse<Void> deleteOrder(@PathVariable UUID orderId, @RequestHeader("X-User-Name") String username) {
        orderCommandService.deleteOrder(orderId, username);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, null);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<OrderResponseDto> getOrder(@PathVariable UUID orderId) {
        OrderResponseDto response = orderQueryService.getOrder(orderId);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

    // 주문 목록 조회 + 검색
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public CustomResponse<Page<OrderSummaryResponseDto>> searchOrders(
            @RequestParam(required = false) UUID orderUserId,
            @RequestParam(required = false) UUID requsetVendorId,
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String status,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        OrderSearchConditionRequestDto condition = new OrderSearchConditionRequestDto(
                orderUserId,
                requsetVendorId,
                productId,
                productName,
                status != null ? Enum.valueOf(OrderStatus.class, status) : null
        );
        Page<OrderSummaryResponseDto> response = orderQueryService.searchOrders(condition, pageable);
        return CustomResponse.onSuccess(GeneralSuccessCode.OK, response);
    }

}
