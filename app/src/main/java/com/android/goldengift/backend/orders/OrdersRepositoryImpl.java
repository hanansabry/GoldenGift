package com.android.goldengift.backend.orders;

import com.android.goldengift.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class OrdersRepositoryImpl implements OrdersRepository {

    private static final String ORDERS = "orders";
    private final DatabaseReference mDatabase;
    private String storeId;

    public OrdersRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference(ORDERS);
        storeId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void retrieveOrdersForCurrentStore(final RetrievingOrdersCallback callback) {
        mDatabase.child(storeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Order> orders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    orders.add(order);
                }
                callback.onRetrieveOrdersSuccessfully(orders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRetrievedOrdersFailed(databaseError.getMessage());
            }
        });
    }

    @Override
    public void retrieveOrderByNumber(String orderNumber, RetrievingOrdersCallback callback) {

    }

    @Override
    public void requestNewOrder(Order order, OrdersRequestCallback callback) {

    }
}
