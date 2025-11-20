package org.pokeherb.orderservice.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.application.service.dto.request.OrderCreateRequestDto;
import org.pokeherb.orderservice.domain.OrderRepository;
import org.pokeherb.orderservice.domain.command.OrderCreateCommand;
import org.pokeherb.orderservice.domain.exception.OrderErrorCode;
import org.pokeherb.orderservice.global.domain.Auditable;
import org.pokeherb.orderservice.global.infrastructure.exception.CustomException;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Access(AccessType.FIELD)
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Order extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 30)
    private OrderStatus orderStatus;

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @Column(name = "request_memo", length = 1000)
    private String requestMemo;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "cancelled_by")
    private UUID cancelledBy;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name= "delivery_driver_id")
    private UUID deliveryDriverId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name="start_hub_id")
    private Long startHubId;

    @Column(name = "end_hub_id")
    private Long endHubId;

    @Column(name = "order_user_id", nullable = false)
    private UUID orderUserId;

    @Column(name = "request_vendor_id")
    private UUID requestVendorId;

    @Column(name = "receive_vendor_id")
    private UUID receiveVendorId;

    @Builder
    private Order(
            UUID id,
            OrderStatus orderStatus,
            LocalDateTime dueAt,
            String requestMemo,
            int quantity,
            String productName,
            UUID cancelledBy,
            LocalDateTime cancelledAt,
            UUID deliveryDriverId,
            UUID productId,
            Long startHubId,
            Long endHubId,
            UUID orderUserId,
            UUID requestVendorId,
            UUID receiveVendorId
    ) {
        this.id = id;
        this.orderStatus = (orderStatus != null) ? orderStatus : OrderStatus.CREATED;
        this.dueAt = dueAt;
        this.requestMemo = requestMemo;
        this.quantity = quantity;
        this.productName = productName;
        this.cancelledBy = cancelledBy;
        this.cancelledAt = cancelledAt;
        this.deliveryDriverId = deliveryDriverId;
        this.productId = productId;
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.orderUserId = orderUserId;
        this.requestVendorId = requestVendorId;
        this.receiveVendorId = receiveVendorId;
    }

    public static Order create(OrderCreateCommand command){
        return Order.builder()
                .productId(command.productId())
                .quantity(command.quantity())
                .orderUserId(command.orderUserId())
                .productName(command.productName())
                .orderStatus(OrderStatus.CREATED)
                .dueAt(command.dueAt())
                .requestMemo(command.requestMemo())
                .startHubId(command.startHubId())
                .endHubId(command.endHubId())
                .requestVendorId(command.requestVendorId())
                .receiveVendorId(command.receiveVendorId())
                .build();
    }

    public void cancelOrder(UUID canceller, LocalDateTime cancelledAt){
        ensureNotDeleted();
        if(!this.orderStatus.isCancellable()){
            throw new CustomException(OrderErrorCode.ORDER_CANNOT_BE_CANCELLED);
        }
        this.orderStatus = OrderStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
        this.updatedAt = LocalDateTime.now();
        this.cancelledBy = canceller;
    }

    public void completeOrder(){
        ensureNotDeleted();
        if (!this.orderStatus.canComplete()) {
            throw new CustomException(OrderErrorCode.ORDER_CANNOT_BE_COMPLETED);
        }
        this.orderStatus = OrderStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete(String username) {
        softDelete(username);
    }

    public void updateOrderInfo(String productName, Integer quantity, String requestMemo, LocalDateTime dueAt) {
        ensureNotDeleted();

        if (!this.orderStatus.isEditable()) {
            throw new CustomException(OrderErrorCode.ORDER_CANNOT_BE_UPDATED);
        }

        if (productName != null && !productName.isBlank()) {
            this.productName = productName;
        }
        if (quantity != null && quantity > 0) {
            this.quantity = quantity;
        }
        if (requestMemo != null) {
            this.requestMemo = requestMemo;
        }
        if (dueAt != null) {
            this.dueAt = dueAt;
        }

        this.updatedAt = LocalDateTime.now();
    }
    private void ensureNotDeleted() {
        if (this.deletedAt != null) {
            throw new CustomException(OrderErrorCode.ORDER_ALREADY_DELETED);
        }
    }
}
