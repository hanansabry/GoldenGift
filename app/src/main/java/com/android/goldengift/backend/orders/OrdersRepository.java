package com.android.goldengift.backend.orders;

import com.android.goldengift.model.Order;

import java.util.ArrayList;

public interface OrdersRepository {

    interface RetrievingOrdersCallback {

        void onRetrieveOrdersSuccessfully(ArrayList<Order> orders);

        void onRetrievedOrdersFailed(String errmsg);
    }

    interface OrdersRequestCallback {

        void onRequestNewOrderSuccessfully();

        void onRequestNewOrderFailed(String errmsg);
    }

    void retrieveOrdersForCurrentStore(RetrievingOrdersCallback callback);

    void retrieveOrderByNumber(String orderNumber, RetrievingOrdersCallback callback);

    void requestNewOrder(Order order, OrdersRequestCallback callback);

    void updateOrderStatus(String orderNumber, Order.OrderStatus status);

    void updateOrderDeliveryTime(String orderNumber, String deliveryTime);

    void updateOrderPaymentMethod(String orderNumber, String paymentMethod);
}
