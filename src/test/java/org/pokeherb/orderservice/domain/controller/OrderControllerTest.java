package org.pokeherb.orderservice.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pokeherb.orderservice.application.service.dto.request.OrderSearchConditionRequestDto;
import org.pokeherb.orderservice.application.service.dto.request.OrderUpdateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderResponseDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderSummaryResponseDto;
import org.pokeherb.orderservice.domain.entity.OrderStatus;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.pokeherb.orderservice.application.command.OrderCommandService;
import org.pokeherb.orderservice.application.query.OrderQueryService;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.application.service.dto.response.OrderCreateResponseDto;
import org.pokeherb.orderservice.global.infrastructure.success.GeneralSuccessCode;
import org.pokeherb.orderservice.presentation.controller.OrderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderCommandService orderCommandService;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @Test
    @WithMockUser
    @DisplayName("컨트롤러 : 주문 생성 성공")
    void createOrder_success() throws Exception {
        UUID productId = UUID.randomUUID();
        UUID orderUserId = UUID.randomUUID();
        UUID requestVendorId = UUID.randomUUID();
        UUID receiveVendorId = UUID.randomUUID();

        OrderCreateRequestDto request = new OrderCreateRequestDto(
                productId,
                3,
                orderUserId,
                "테스트 상품",
                LocalDateTime.now().plusHours(2),
                "테스트 메모",
                1L,
                2L,
                requestVendorId,
                receiveVendorId
        );

        OrderCreateResponseDto response = org.mockito.Mockito.mock(OrderCreateResponseDto.class);
        given(orderCommandService.createOrder(any(OrderCreateRequestDto.class))).willReturn(response);

        mockMvc.perform(post("/v1/order", UUID.randomUUID())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result").exists());

        verify(orderCommandService).createOrder(any(OrderCreateRequestDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 주문 수정 성공")
    void updateOrder() throws Exception {
        UUID orderId = UUID.randomUUID();

        OrderUpdateRequestDto request = new OrderUpdateRequestDto(
                "수정된 상품",
                5,
                "수정된 메모",
                LocalDateTime.now().plusHours(3)
        );
        OrderCreateResponseDto response = org.mockito.Mockito.mock(OrderCreateResponseDto.class);
        given(orderCommandService.updateOrder(eq(orderId), any(OrderUpdateRequestDto.class))).willReturn(response);
        mockMvc.perform(patch("/v1/order/{orderId}", orderId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result").exists());
        verify(orderCommandService).updateOrder(eq(orderId), any(OrderUpdateRequestDto.class));
    }

    @Test
    @WithMockUser(username = "test-user", roles = "USER")
    @DisplayName("컨트롤러 : 주문 취소 성공")
    void cancelOrder() throws Exception{
        UUID orderId = UUID.randomUUID();
        UUID canellerId =  UUID.randomUUID();

        OrderResponseDto mockResoponse = org.mockito.Mockito.mock(OrderResponseDto.class);
        given(orderCommandService.cancelOrder(eq(orderId), eq(canellerId))).willReturn(mockResoponse);
        mockMvc.perform(post("/v1/order/{orderId}/cancel", orderId)
                    .with(csrf())
                    .header("X-User-Id", canellerId.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result").exists());
        verify(orderCommandService).cancelOrder(eq(orderId), eq(canellerId));
    }

    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 주문 삭제 성공")
    void deleteOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        String username = "testuser";
        mockMvc.perform(delete("/v1/order/{orderId}", orderId)
                    .with(csrf())
                    .header("X-User-name", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()));
        verify(orderCommandService).deleteOrder(eq(orderId), eq(username));
    }

    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 주문 상세 조회 성공")
    void getOrder() throws Exception{
        UUID orderId = UUID.randomUUID();

        OrderResponseDto mockResoponse = org.mockito.Mockito.mock(OrderResponseDto.class);
        given(orderQueryService.getOrder(eq(orderId))).willReturn(mockResoponse);

        mockMvc.perform(get("/v1/order/{orderId}", orderId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result").exists());
        verify(orderQueryService).getOrder(eq(orderId));
    }

    @Test
    @WithMockUser
    @DisplayName("컨트롤러: 주문 목록 검색 + 페지징 조회 성공")
    void searchOder() throws Exception{
        UUID orderUserId = UUID.randomUUID();

        OrderSummaryResponseDto dto1 = org.mockito.Mockito.mock(OrderSummaryResponseDto.class);
        OrderSummaryResponseDto dto2 = org.mockito.Mockito.mock(OrderSummaryResponseDto.class);

        Page<OrderSummaryResponseDto> page = new PageImpl<>(Arrays.asList(dto1, dto2), PageRequest.of(0,10), 2);
        given(orderQueryService.searchOrders(any(OrderSearchConditionRequestDto.class), any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/v1/order")
                        .with(csrf())
                    .param("orderUserId", orderUserId.toString())
                    .param("productName", "상품")
                    .param("status", OrderStatus.CREATED.name())
                    .param("page", "0")
                    .param("size", "10")
                    .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result.content").isArray())
                .andExpect(jsonPath("$.result.totalElements").value(2));
        verify(orderQueryService).searchOrders(any(OrderSearchConditionRequestDto.class), any(Pageable.class));
    }
}
