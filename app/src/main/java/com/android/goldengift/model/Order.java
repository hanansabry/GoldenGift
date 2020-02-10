package com.android.goldengift.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Order implements Parcelable {

    private String id;
    private Long orderNumber;
    private String phoneNumber;
    private String date;
    private String address;
    private String status;
    private HashMap<String, OrderItem> orderItems;
    private double totalCost;

    public Order() {
    }

    protected Order(Parcel in) {
        id = in.readString();
        if (in.readByte() == 0) {
            orderNumber = null;
        } else {
            orderNumber = in.readLong();
        }
        phoneNumber = in.readString();
        date = in.readString();
        address = in.readString();
        status = in.readString();
        orderItems = (HashMap<String, OrderItem>) in.readSerializable();
        totalCost = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public HashMap<String, OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(HashMap<String, OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        if (orderNumber == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(orderNumber);
        }
        dest.writeString(phoneNumber);
        dest.writeString(date);
        dest.writeString(address);
        dest.writeString(status);
        dest.writeSerializable(orderItems);
        dest.writeDouble(totalCost);
    }
}
