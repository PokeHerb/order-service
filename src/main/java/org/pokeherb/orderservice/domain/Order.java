package org.pokeherb.orderservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private long startHubId;

    @Column(name = "end_hub_id")
    private long endHubId;

    @Column(name = "order_user_id", nullable = false)
    private UUID orderUserId;

    @Column(name = "request_vendor_id")
    private UUID requestVendorId;

    @Column(name = "receive_vendor_id")
    private UUID receiveVendorId;

    private Order(
            UUID productId,
            int quantity,
            UUID orderUserId,
            String productName,
            LocalDateTime dueAt,
            String requestMemo,
            long startHubId,
            long endHubId,
            UUID requestVendorId,
            UUID receiveVendorId
    ){
        if(quantity <= 0){
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        this.productId = productId;
        this.quantity = quantity;
        this.orderUserId = orderUserId;
        this.productName = productName;
        this.dueAt = dueAt;
        this.requestMemo = requestMemo;
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.requestVendorId = requestVendorId;
        this.receiveVendorId = receiveVendorId;
        this.orderStatus = OrderStatus.CREATED;
    }

    public static Order create(
            UUID productId,
            int quantity,
            UUID orderUserId,
            String productName,
            LocalDateTime dueAt,
            String requestMemo,
            long startHubId,
            long endHubId,
            UUID requestVendorId,
            UUID receiveVendorId
    ) {
        // 필수값 검증
        if (productId == null) {
            throw new CustomException(OrderErrorCode.INVALID_PRODUCT);
        }
        if (orderUserId == null) {
            throw new CustomException(OrderErrorCode.INVALID_ORDER_USER);
        }
        if (productName == null || productName.isBlank()) {
            throw new CustomException(OrderErrorCode.INVALID_PRODUCT_NAME);
        }

        // 수량 검증 (int 이므로 null 체크 X)
        if (quantity <= 0) {
            throw new CustomException(OrderErrorCode.INVALID_QUANTITY);
        }

        // 납기 일 검증
        if (dueAt != null && dueAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(OrderErrorCode.INVALID_DUE_DATE);
        }

        return new Order(
                productId,
                quantity,
                orderUserId,
                productName,
                dueAt,
                requestMemo,
                startHubId,
                endHubId,
                requestVendorId,
                receiveVendorId
        );
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

    public void delete(String deletedBy, LocalDateTime deletedAt) {
        ensureNotDeleted();
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
        this.updatedAt = deletedAt;
    }

    public void updateOrderInfo(String productName, Integer quantity, String requestMemo, LocalDateTime dueAt) {
        ensureNotDeleted();

        if (!this.orderStatus.isEditable()) {
            throw new CustomException(OrderErrorCode.ORDER_CANNOT_BE_COMPLETED);
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
