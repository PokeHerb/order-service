package org.pokeherb.orderservice.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.pokeherb.orderservice.domain.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/order")
@RequiredArgsConstructor
public class OrderController {

//    // 주문 생성
//    @PostMapping("/{orderId}")
//    public ResponseEntity<String> createOrder(@RequestBody Order order){
//
//    }
//
//    // 주문 상세 조회
//    @GetMapping("/{orderId}")
//    public ResponseEntity<List<Order>> getOrderDetails(){
//
//    }
//
//    // 주문 검색, 목록 조회
//    @GetMapping
//    public ResponseEntity getAllOrders(){
//
//    }
//
//    // 주문 취소
//    @PatchMapping("{orderId}/cancel")
//    public ResponseEntity cancelOrder(@RequestBody Order order){
//
//    }
//
//    // 주문 삭제
//    @DeleteMapping("{orderId}")
//    public ResponseEntity deleteOrder(@RequestBody Order order){
//
//    }
//
//    // 주문 수정
//    @PatchMapping("{orderId}")
//    public ResponseEntity updateOrder(@RequestBody Order order){
//
//    }
}
