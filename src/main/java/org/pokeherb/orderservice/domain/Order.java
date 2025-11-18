package org.pokeherb.orderservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pokeherb.orderservice.global.domain.Auditable;
import org.springframework.cglib.core.Local;

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
    private UUID startHubId;

    @Column(name = "end_hub_id")
    private UUID endHubId;

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
            UUID startHubId,
            UUID endHubId,
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
            UUID startHubId,
            UUID endHubId,
            UUID requestVendorId,
            UUID receiveVendorId
    ) {
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

    public void assignDriver(UUID driverId){
        if(this.orderStatus == OrderStatus.CANCELLED || this.orderStatus == OrderStatus.COMPLETED){
            throw new IllegalStateException("cannot assign driver to finished order");
        }
        this.deliveryDriverId = driverId;
        this.orderStatus = OrderStatus.ASSIGNED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancelOrder(UUID canceller, LocalDateTime cancelledAt){
        if(!this.orderStatus.isCancellable()){
            throw new IllegalStateException("order cannot be canelled in status: " + orderStatus);
        }
        this.orderStatus = OrderStatus.CANCELLED;
        this.cancelledAt = cancelledAt;
        this.updatedAt = LocalDateTime.now();
        this.cancelledBy = canceller;
    }

    public void completeOrder(){
        if(this.orderStatus == OrderStatus.COMPLETED){
            throw new IllegalStateException("order cannot be completed in status: " + orderStatus);
        }
        this.orderStatus = OrderStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete(String deletedBy, LocalDateTime deletedAt) {
        this.deletedBy = deletedBy;
        this.deletedAt = deletedAt;
        this.updatedAt = deletedAt;
    }

}
