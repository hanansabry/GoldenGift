package com.android.goldengift.customer.payment;

import com.android.goldengift.backend.orders.OrdersRepository;
import com.android.goldengift.model.Order;

public class PaymentPresenter {

    private final OrdersRepository ordersRepository;

    public PaymentPresenter(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public void requestNewOrder(Order order, OrdersRepository.OrdersRequestCallback callback) {
        ordersRepository.requestNewOrder(order, callback);
    }

}
