package org.pokeherb.orderservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository{
    Order save(Order order);

    Optional<Order> findById(UUID id);

    boolean existsById(UUID id);
}
