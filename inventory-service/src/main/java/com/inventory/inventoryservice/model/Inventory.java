package com.inventory.inventoryservice.model;

import jakarta.persistence.*;

@Entity
@Table(name="inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="product_name",nullable = false)
    private String productName;

    @Column(name="available_quantity",nullable = false)
    private Integer availableQuantity;

    @Column(name="price",nullable = false)
    private Double price;

    public Inventory(String productName, Integer availableQuantity, Double price) {
        this.productName = productName;
        this.availableQuantity = availableQuantity;
        this.price = price;
    }

    public Inventory() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
