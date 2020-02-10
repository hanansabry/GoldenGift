package com.android.goldengift.store.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.goldengift.EmptyRecyclerView;
import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.orders.OrdersRepository;
import com.android.goldengift.model.Order;
import com.android.goldengift.store.orders.order_invoice.OrderInvoiceActivity;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OrdersActivity extends AppCompatActivity implements OrdersRepository.RetrievingOrdersCallback, OrdersPresenter.OrderCallback {

    private OrdersPresenter presenter;
    private OrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new OrdersPresenter(Injection.provideOrdersRepoisotry());
        presenter.retrieveStoreOrders(this);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        EmptyRecyclerView ordersRV = findViewById(R.id.orders_recyclerview);
        ordersRV.setLayoutManager(new LinearLayoutManager(this));
        ordersRV.setEmptyView(findViewById(R.id.empty_view));
        ordersAdapter = new OrdersAdapter(presenter, this);
        ordersRV.setAdapter(ordersAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onRetrieveOrdersSuccessfully(ArrayList<Order> orders) {
        ordersAdapter.bindOrders(orders);
    }

    @Override
    public void onRetrievedOrdersFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOrderClicked(int position) {
        Order order = presenter.getOrderByPosition(position);
        Intent intent = new Intent(this, OrderInvoiceActivity.class);
        intent.putExtra(Order.class.getName(), order);
        startActivity(intent);
    }
}
