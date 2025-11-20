package org.pokeherb.orderservice.domain.entity;

public enum OrderStatus {

    CREATED,
    ASSIGNED,
    PICKED_UP,
    IN_DELIVERY,
    COMPLETED,
    CANCELLED;

    /**
     * 상태 전환 가능한지 체크하는 도메인 규칙
     */
    public boolean canTransitionTo(OrderStatus target) {

        return switch (this) {
            case CREATED ->
                    target == ASSIGNED || target == CANCELLED;

            case ASSIGNED ->
                    target == PICKED_UP || target == CANCELLED;

            case PICKED_UP ->
                    target == IN_DELIVERY;

            case IN_DELIVERY ->
                    target == COMPLETED;

            case COMPLETED, CANCELLED ->
                    false; // 완료/취소된 주문은 변화 불가
        };
    }

    public boolean canAssignDriver() {
        return this == CREATED;
    }

    public boolean canPickUp() {
        return this == ASSIGNED;
    }

    public boolean canStartDelivery() {
        return this == PICKED_UP;
    }

    public boolean canComplete() {
        return this == IN_DELIVERY;
    }

    public boolean isCancellable() {
        return this == CREATED || this == ASSIGNED;
    }

    public boolean isEditable() {
        return this == CREATED || this == ASSIGNED;
    }
}
