package com.android.goldengift.store.orders.order_invoice;

import com.android.goldengift.model.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderInvoicePresenter {

    private ArrayList<OrderItem> orderItems = new ArrayList<>();


    public int getOrderItemsSize() {
        return orderItems.size();
    }

    public void onBindOrderItemAtPosition(OrderItemsAdapter.OrderItemViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.setOrderItemData(orderItem);
    }

    public void bindOrderItems(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public ArrayList<OrderItem> getOrderItemsAsArrayLit(HashMap<String, OrderItem> orderItemsHashMap) {
        ArrayList<OrderItem> orderItems = new ArrayList<>();

        if (orderItemsHashMap == null) {
            return new ArrayList<>();
        } else {
            for (String key : orderItemsHashMap.keySet()) {
                OrderItem orderItem = orderItemsHashMap.get(key);
                orderItems.add(orderItem);
            }
            return orderItems;
        }
    }
}
