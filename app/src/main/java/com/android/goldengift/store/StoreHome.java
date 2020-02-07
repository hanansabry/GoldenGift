package com.android.goldengift.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.goldengift.R;
import com.android.goldengift.store.categories.CategoriesActivity;
import com.android.goldengift.store.orders.OrdersActivity;
import com.android.goldengift.store.products.ProductsActivity;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

public class StoreHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_home);
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    public void onOrdersClicked(View view) {
        startActivity(new Intent(this, OrdersActivity.class));
    }

    public void onCategoriesClicked(View view) {
        startActivity(new Intent(this, CategoriesActivity.class));
    }

    public void onProductsClicked(View view) {
        startActivity(new Intent(this, ProductsActivity.class));
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
