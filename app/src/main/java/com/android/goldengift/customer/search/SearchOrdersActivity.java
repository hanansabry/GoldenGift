package com.android.goldengift.customer.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.goldengift.EmptyRecyclerView;
import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.orders.OrdersRepository;
import com.android.goldengift.model.Order;
import com.android.goldengift.store.orders.OrdersAdapter;
import com.android.goldengift.store.orders.OrdersPresenter;
import com.android.goldengift.store.orders.order_invoice.OrderInvoiceActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class SearchOrdersActivity extends AppCompatActivity implements OrdersPresenter.OrderCallback, OrdersRepository.RetrievingOrdersCallback {

    private OrdersAdapter ordersAdapter;
    private OrdersPresenter presenter;
    private EditText phoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_orders);

        phoneNumberEditText = findViewById(R.id.phone_number_edittext);
        presenter = new OrdersPresenter(Injection.provideOrdersRepoisotry());
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        EmptyRecyclerView ordersRV = findViewById(R.id.orders_recyclerview);
        ordersRV.setLayoutManager(new LinearLayoutManager(this));
        ordersRV.setEmptyView(findViewById(R.id.empty_view));
        ordersAdapter = new OrdersAdapter(presenter, this);
        ordersRV.setAdapter(ordersAdapter);
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onOrderClicked(int position) {
        Order order = presenter.getOrderByPosition(position);
        Intent intent = new Intent(this, OrderInvoiceActivity.class);
        intent.putExtra("IsStore", false);
        intent.putExtra(Order.class.getName(), order);
        startActivity(intent);
    }

    @Override
    public void onRetrieveOrdersSuccessfully(ArrayList<Order> orders) {
        ordersAdapter.bindOrders(orders);
    }

    @Override
    public void onRetrievedOrdersFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }

    public void onSearchClicked(View view) {
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        presenter.searchOrdersByPhoneNumber(phoneNumber, this);
    }
}
