package org.pokeherb.orderservice.domain.entity;

public enum OrderStatus {

    CREATED,    //주문 생성
    ASSIGNED,   // 기사 배정
    IN_DELIVERY, // 배송 중(허브와 허브 이동 포함)
    COMPLETED,   // 배송 완료
    CANCELLED;  // 주문 취소(CREATED, ASSIGNED까지만 취소 가능)
    /**
     * 상태 전환 가능한지 체크하는 도메인 규칙
     */
    public boolean canTransitionTo(OrderStatus target) {
        return switch (this) {
            case CREATED ->
                    target == ASSIGNED || target == CANCELLED;

            case ASSIGNED ->
                    target == IN_DELIVERY || target == CANCELLED;

            case IN_DELIVERY ->
                    target == COMPLETED;

            case COMPLETED, CANCELLED ->
                    false; // 완료/취소된 주문은 변화 불가
        };
    }

    public boolean canComplete() {
        return this == IN_DELIVERY;
    }

    public boolean isEditable() {
        return this == CREATED || this == ASSIGNED;
    }
}
