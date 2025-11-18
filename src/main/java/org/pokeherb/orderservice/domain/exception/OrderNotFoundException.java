package org.pokeherb.orderservice.domain.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(UUID id) {
        super("order not found id= " + id);
    }
}
