package com.billing.billingservice.service;

import com.billing.billingservice.repository.BillingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.billing.billingservice.model.Billing;

import java.util.Map;

import com.billing.billingservice.enums.PaymentStatus;

@Service
public class BillingService{

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BillingRepository billingRepository;

    private static final Logger log = LoggerFactory.getLogger(BillingService.class);

    @KafkaListener(topics = "order-topic",groupId = "billing-group-id")
    public void consume(String message){
        log.info("Consumed message: {}",message);

        try {
            // Deserialize the JSON string into a Map
            Map<String, Object> orderMessage = objectMapper.readValue(message, Map.class);

            // Extract data
            Long orderId = Long.valueOf(orderMessage.get("id").toString());
            String productName = (String) orderMessage.get("productName");
            Integer quantity = (Integer) orderMessage.get("quantity");
            Double price = (Double) orderMessage.get("price");
            PaymentStatus paymentStatus = PaymentStatus.valueOf((String) orderMessage.get("paymentStatus"));

            // Create Billing record
            Billing billing = new Billing(orderId, productName, quantity, price, paymentStatus);

            // Save Billing record
            billingRepository.save(billing);

            log.info("Billing record saved for Order ID: {}", orderId);
        } catch (Exception e) {
            log.error("Error while processing the message: {}", e.getMessage());
        }
    }
}
