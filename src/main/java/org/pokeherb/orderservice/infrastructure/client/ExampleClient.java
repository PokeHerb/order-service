package org.pokeherb.orderservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("example-service")
public interface ExampleClient {
}
