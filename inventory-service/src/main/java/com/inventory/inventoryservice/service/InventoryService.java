package com.inventory.inventoryservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.inventoryservice.model.Inventory;
import com.inventory.inventoryservice.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryRepository inventoryRepository;

    @KafkaListener(topics = "order-topic",groupId = "inventory-group-id")
    public void consume(String message){
        log.info("Consumed message: {}", message);

        try {
            // Deserialize the JSON string into a Map
            Map<String,Object> orderMessage = objectMapper.readValue(message, Map.class);

            // Extract data
            Integer id = (Integer) orderMessage.get("id");
            String productName = (String) orderMessage.get("productName");
            Integer quantity = (Integer) orderMessage.get("quantity");
            Double price = (Double) orderMessage.get("price");

            // Log the received message
            System.out.println("Received Order: " + orderMessage);

            // Process the order (e.g., update inventory)
            updateInventory(productName, quantity);

        } catch (Exception e) {
            log.error("Error while deserialize the JSON string. {} ", e.getMessage());
        }
    }

    private void updateInventory(String productName, Integer quantity) {
        Optional<Inventory> inventoryOptional = inventoryRepository.findByProductName(productName);

        if (inventoryOptional.isPresent()) {
            Inventory inventory = inventoryOptional.get();
            int updatedQuantity = inventory.getAvailableQuantity() - quantity;

            // Check if there's enough stock
            if (updatedQuantity < 0) {
                log.error("Not enough stock available for product: {}", productName);
                return;
            }

            // Update the available quantity
            inventory.setAvailableQuantity(updatedQuantity);

            // Save the updated inventory
            inventoryRepository.save(inventory);
            log.info("Inventory updated for product: {}", productName);
        } else {
            log.warn("Product not found in inventory: {}", productName);
        }
    }

}
