package com.android.goldengift.store.orders;

import com.android.goldengift.backend.orders.OrdersRepository;
import com.android.goldengift.model.Order;

import java.util.ArrayList;

public class OrdersPresenter {

    interface OrderCallback {
        void onOrderClicked();
    }

    private ArrayList<Order> orders = new ArrayList<>();
    private final OrdersRepository ordersRepository;

    public OrdersPresenter(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public int getOrdersSize() {
        return orders.size();
    }

    public void onBindOrderAtPosition(OrdersAdapter.OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.setOrderData(order);
    }

    public void bindOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void retrieveStoreOrders(OrdersRepository.RetrievingOrdersCallback callback) {
        ordersRepository.retrieveOrdersForCurrentStore(callback);
    }
}