package com.order.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.orderservice.enums.PaymentStatus;
import com.order.orderservice.model.Order;
import com.order.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String,Object> placeOrder(Order order){

        //Todo. For now save the payment status to COMPLETED
        order.setPaymentStatus(PaymentStatus.COMPLETED);

        //save the order
        Order savedOrder = orderRepository.save(order);

        // Create a map to send as a JSON message to Kafka
        Map<String, Object> orderMessage = new HashMap<>();
        orderMessage.put("id", savedOrder.getId());
        orderMessage.put("productName", savedOrder.getProductName());
        orderMessage.put("quantity", savedOrder.getQuantity());
        orderMessage.put("price", savedOrder.getPrice());
        orderMessage.put("paymentStatus", savedOrder.getPaymentStatus());

        log.info("Order Message: {}", orderMessage );
        try {
            // Convert the Map to a JSON string
            String messageAsJson = objectMapper.writeValueAsString(orderMessage);

            // Send the JSON string to Kafka
            kafkaTemplate.send("order-topic", messageAsJson);
            log.info("Message published successfully. Message: {}",messageAsJson);
        } catch (Exception e) {
            log.error("Failed to serialize order message to JSON", e);
        }
        return orderMessage;
    }
}

