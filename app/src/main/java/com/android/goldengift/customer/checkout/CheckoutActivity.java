package com.android.goldengift.customer.checkout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.goldengift.EmptyRecyclerView;
import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.orders.OrdersRepository;
import com.android.goldengift.customer.payment.PaymentActivity;
import com.android.goldengift.model.Order;
import com.android.goldengift.model.OrderItem;
import com.android.goldengift.store.orders.order_invoice.OrderInvoicePresenter;
import com.android.goldengift.store.orders.order_invoice.OrderItemsAdapter;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CheckoutActivity extends AppCompatActivity implements OrdersRepository.OrdersRequestCallback {

    private EditText phoneNumber, customerName, address;
    private TextView orderNumberET, totalCost;
    private Button doneButton;
    private CheckoutPresenter presenter;
    private long orderNumber;
    private HashMap<String, OrderItem> orderItemHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        presenter = new CheckoutPresenter(this, Injection.provideInvoiceRepository(sharedPreferences), Injection.provideOrdersRepoisotry());

        orderNumber = presenter.generateOrderNumber();
        orderItemHashMap = presenter.getOrderItemsAsHashMap();
        initializeViews();
        initializeOrderItemsRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void initializeViews() {
        phoneNumber = findViewById(R.id.phone_number);
        customerName = findViewById(R.id.customer_name);
        address = findViewById(R.id.address);

        orderNumberET = findViewById(R.id.order_number);
        orderNumberET.setText(String.valueOf(orderNumber));

        totalCost = findViewById(R.id.total_cost_textview);
        totalCost.setText(String.valueOf(presenter.getOrderTotalCost()));
    }

    private void initializeOrderItemsRecyclerView() {
        EmptyRecyclerView orderItemsRV = findViewById(R.id.order_items_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        orderItemsRV.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderItemsRV.getContext(),
                layoutManager.getOrientation());
        orderItemsRV.addItemDecoration(dividerItemDecoration);

        OrderInvoicePresenter orderInvoicePresenter = new OrderInvoicePresenter();
        OrderItemsAdapter orderItemsAdapter = new OrderItemsAdapter(orderInvoicePresenter);
        orderItemsRV.setAdapter(orderItemsAdapter);
        orderItemsAdapter.bindOrderItems(orderInvoicePresenter.getOrderItemsAsArrayLit(orderItemHashMap));
    }

    private Order getOrderRequestInfo() {
        String phone = phoneNumber.getText().toString().trim();
        String name = customerName.getText().toString().trim();
        String add = address.getText().toString().trim();

        //get order items
        Order order = new Order();
        order.setPhoneNumber(phone);
        order.setAddress(add);
        order.setCustomerName(name);
        order.setDate(presenter.getCurrentDate());
        order.setOrderNumber(orderNumber);
        order.setStatus(Order.OrderStatus.New.name());
        order.setTotalCost(presenter.getOrderTotalCost());
        order.setOrderItems(orderItemHashMap);

        return order;
    }

    public void onDoneClicked(View view) {
        Order order = getOrderRequestInfo();
        if (presenter.validateOrderInfo(order)) {
            presenter.requestNewOrder(order, this);
        }
    }

    public void showAddressIsRequiredMsg() {
        address.setError("Address is required");
    }

    public void showCustomerNameIsRequiredMsg() {
        customerName.setError("Name is required");
    }

    public void showPhoneNumberIsRequiredMsg() {
        phoneNumber.setError("Phone is required");
    }

    @Override
    public void onRequestNewOrderSuccessfully() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestNewOrderFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show();
    }
}
