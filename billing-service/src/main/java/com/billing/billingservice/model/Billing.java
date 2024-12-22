package com.billing.billingservice.model;

import com.billing.billingservice.enums.PaymentStatus;
import jakarta.persistence.*;

@Entity
@Table
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId; // Maps to the id of the corresponding Order

    private String productName;
    private int quantity;
    private double price;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public Billing(Long orderId, String productName, int quantity, double price, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.paymentStatus = paymentStatus;
    }

    public Billing() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
