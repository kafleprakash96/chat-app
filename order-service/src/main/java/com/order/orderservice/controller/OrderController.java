package com.order.orderservice.controller;

import com.order.orderservice.model.Order;
import com.order.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<Map<String,Object>> placeOrder(@RequestBody Order order){
        Map<String, Object> response = this.orderService.placeOrder(order);
        return ResponseEntity.ok(response);
    }
}
