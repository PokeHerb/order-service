package org.pokeherb.orderservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum OrderStatus {
    CREATED,
    ASSIGNED,
    PICKED_UP,
    IN_DELIVERY,
    COMPLETED,
    CANCELLED;

    public boolean isCancellable() {
        return this == CREATED || this == ASSIGNED;
    }
    public boolean isEditable() {
        return this == CREATED || this == ASSIGNED;
    }
}