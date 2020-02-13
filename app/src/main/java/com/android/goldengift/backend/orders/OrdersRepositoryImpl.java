package com.android.goldengift.backend.orders;

import com.android.goldengift.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class OrdersRepositoryImpl implements OrdersRepository {

    private static final String ORDERS = "orders";
    private final DatabaseReference mDatabase;
//    private String storeId;

    public OrdersRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
//        storeId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void retrieveOrdersForCurrentStore(final RetrievingOrdersCallback callback) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Order> orders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    orders.add(order);
                }
                callback.onRetrieveOrdersSuccessfully(sortByDate(orders));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievedOrdersFailed(databaseError.getMessage());
            }
        });
    }

    private ArrayList<Order> sortByDate(ArrayList<Order> orders) {
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return orders;
    }

    @Override
    public void retrieveOrderByPhoneNumber(String phoneNumber, final RetrievingOrdersCallback callback) {
        mDatabase.orderByChild("phoneNumber")
                .equalTo(phoneNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Order> orders = new ArrayList<>();
                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            orders.add(order);
                        }
                        callback.onRetrieveOrdersSuccessfully(sortByDate(orders));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onRetrievedOrdersFailed(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void requestNewOrder(Order order, final OrdersRequestCallback callback) {
        mDatabase.child(order.getOrderNumber().toString()).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onRequestNewOrderSuccessfully();
                } else {
                    callback.onRequestNewOrderFailed(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void updateOrderStatus(String orderNumber, Order.OrderStatus status) {
        HashMap<String, Object> orderStatusValue = new HashMap<>();
        orderStatusValue.put("status", status);
        mDatabase.child(orderNumber).updateChildren(orderStatusValue);
    }

    @Override
    public void updateOrderDeliveryTime(String orderNumber, String deliveryTime) {
        HashMap<String, Object> deliveryTimeValue = new HashMap<>();
        deliveryTimeValue.put("deliveryTime", deliveryTime);
        mDatabase.child(orderNumber).updateChildren(deliveryTimeValue);
    }

    @Override
    public void updateOrderPaymentMethod(String orderNumber, String paymentMethod) {
        HashMap<String, Object> paymentMethodValue = new HashMap<>();
        paymentMethodValue.put("paymentMethod", paymentMethodValue);
        mDatabase.child(orderNumber).updateChildren(paymentMethodValue);
    }
}
