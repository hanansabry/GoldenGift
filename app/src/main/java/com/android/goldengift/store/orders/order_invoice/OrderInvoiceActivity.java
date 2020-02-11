package com.android.goldengift.store.orders.order_invoice;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.goldengift.EmptyRecyclerView;
import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.model.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OrderInvoiceActivity extends AppCompatActivity {

    private TextView orderNumber, date, phoneNumber, address, status, totalCost;
    private OrderInvoicePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_invoice);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Order order = intent.getParcelableExtra(Order.class.getName());

        presenter = new OrderInvoicePresenter(Injection.provideOrdersRepoisotry());
        initializeViews(order);
        initializeOrderItemsRecyclerView(order);
    }

    private void initializeViews(Order order) {
        orderNumber = findViewById(R.id.order_number);
        orderNumber.setText(String.valueOf(order.getOrderNumber()));

        date = findViewById(R.id.order_date);
        date.setText(order.getDate());

        phoneNumber = findViewById(R.id.phone_number);
        phoneNumber.setText(order.getPhoneNumber());

        address = findViewById(R.id.address);
        address.setText(order.getAddress());

        status = findViewById(R.id.status);
        status.setText(order.getStatus());

        totalCost = findViewById(R.id.order_total_cost);
        totalCost.setText(String.format("Total Cost : %s", order.getTotalCost()));
    }

    private void initializeOrderItemsRecyclerView(Order order) {
        EmptyRecyclerView rv = findViewById(R.id.order_items_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                layoutManager.getOrientation());
        rv.addItemDecoration(dividerItemDecoration);

        OrderItemsAdapter adapter = new OrderItemsAdapter(presenter);
        rv.setAdapter(adapter);
        adapter.bindOrderItems(presenter.getOrderItemsAsArrayLit(order.getOrderItems()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_order_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.accept_action) {
            presenter.updateOrderStatusToPending(orderNumber.getText().toString());
            Toast.makeText(this, "Order is accepted", Toast.LENGTH_LONG).show();
            finish();
        }
        return true;
    }
}
