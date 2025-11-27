package org.pokeherb.orderservice.domain.command;

import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreateCommand (
    UUID productId,
    int quantity,
    UUID orderUserId,
    String productName,
    LocalDateTime dueAt,
    String requestMemo,
    Long startHubId,
    Long endHubId,
    UUID requestVendorId,
    UUID receiveVendorId,
    String vendorAddress,
    UUID receiverSlackId,
    String receiverName
){
    public OrderCreateCommand {
        // ⚠ 여기서부터는 "도메인 규칙"에 가까운 것들을 검증

        if (quantity <= 0) {
            throw new CustomException(OrderErrorCode.INVALID_QUANTITY);
        }

        if (productName == null || productName.isBlank()) {
            throw new CustomException(OrderErrorCode.INVALID_PRODUCT_NAME);
        }

        if (dueAt != null && dueAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(OrderErrorCode.INVALID_DUE_DATE);
        }

        // productId, orderUserId 는 아래에서 서비스 레벨 검증으로 남겨둘 거라
        // 여기서는 null 체크를 일부러 안 해도 됨 (팀 정책에 따라 여기로 올려도 됨)
    }
}
