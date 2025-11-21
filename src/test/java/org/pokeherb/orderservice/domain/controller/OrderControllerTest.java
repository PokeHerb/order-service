package org.pokeherb.orderservice.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        mockMvc.perform(post("/v1/order/{orderId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)          // ✅ 이제 인식됨
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(GeneralSuccessCode.OK.getCode()))
                .andExpect(jsonPath("$.result").exists());

        verify(orderCommandService).createOrder(any(OrderCreateRequestDto.class));
    }

}
