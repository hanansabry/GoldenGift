package com.android.goldengift.customer.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.orders.OrdersRepository;
import com.android.goldengift.intro.IntroActivity;
import com.android.goldengift.model.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class OnDeliveryActivity extends AppCompatActivity implements OrdersRepository.OrdersRequestCallback {

    private PaymentPresenter presenter;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_delivery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order = getIntent().getParcelableExtra(Order.class.getName());
        presenter = new PaymentPresenter(Injection.provideOrdersRepoisotry());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void onClickPlaceOrder(View view) {
        presenter.requestNewOrder(order, this);
    }

    @Override
    public void onRequestNewOrderSuccessfully() {
        Toast.makeText(this, "Order is requested successfully", Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(this, IntroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onRequestNewOrderFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }
}
