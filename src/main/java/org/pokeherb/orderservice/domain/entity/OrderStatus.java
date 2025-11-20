package org.pokeherb.orderservice.domain.entity;

public enum OrderStatus {
    CREATED,
    ASSIGNED,
    PICKED_UP,
    IN_DELIVERY,
    COMPLETED,
    CANCELLED;

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
