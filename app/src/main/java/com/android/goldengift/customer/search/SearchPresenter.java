package com.android.goldengift.customer.search;

import com.android.goldengift.backend.orders.OrdersRepository;

public class SearchPresenter {

    private final OrdersRepository ordersRepository;

    public SearchPresenter(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public void searchOrdersByPhoneNumber(String phoneNumber, OrdersRepository.RetrievingOrdersCallback callback) {
        ordersRepository.retrieveOrderByPhoneNumber(phoneNumber, callback);
    }
}
