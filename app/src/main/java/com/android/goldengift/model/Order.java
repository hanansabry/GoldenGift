package com.android.goldengift.model;

import android.widget.ArrayAdapter;

public class Order {

    private String id;
    private Long orderNumber;
    private String phoneNumber;
    private String date;
    private String address;
    private String status;
    private ArrayAdapter<OrderItem> orderItems;
    private double totalCost;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayAdapter<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayAdapter<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
