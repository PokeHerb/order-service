package org.pokeherb.orderservice.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.pokeherb.orderservice.global.infrastructure.error.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER404", "주문을 찾을 수 없습니다."),
    ORDER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "ORDER400_1", "이미 삭제된 주문입니다."),
    ORDER_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "ORDER400_2", "현재 상태에서는 주문을 취소할 수 없습니다."),
    ORDER_CANNOT_BE_COMPLETED(HttpStatus.BAD_REQUEST, "ORDER400_3", "현재 상태에서는 주문을 완료할 수 없습니다."),
    ORDER_CANNOT_BE_UPDATED(HttpStatus.BAD_REQUEST, "ORDER400_4", "업데이트를 할 수 없습니다."),

    // 생성 시 검증용
    INVALID_ORDER_USER(HttpStatus.BAD_REQUEST, "ORDER400_5", "주문자가 존재해야 합니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "ORDER400_6", "수량은 1 이상이어야 합니다."),
    INVALID_DUE_DATE(HttpStatus.BAD_REQUEST, "ORDER400_7", "납기일이 과거입니다."),
    INVALID_PRODUCT(HttpStatus.BAD_REQUEST, "ORDER400_8", "상품 정보가 올바르지 않습니다."),
    INVALID_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "ORDER400_9", "상품 이름이 없습니다."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST, "ORDER400_10", "잘못된 주문 상태 입니다."),
    INVALID_STATUS_TRANSITION(HttpStatus.BAD_REQUEST, "ORDER400_10", "유효하지 않은 상태 전환입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
