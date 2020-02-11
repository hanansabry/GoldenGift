package com.android.goldengift.customer.category_products;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.products.ProductsRepository;
import com.android.goldengift.customer.checkout.CheckoutActivity;
import com.android.goldengift.model.Category;
import com.android.goldengift.model.Product;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryProductsActivity extends AppCompatActivity implements ProductsRepository.ProductsRetrievingCallback {

    private CategoryProductsPresenter presenter;
    private ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        presenter = new CategoryProductsPresenter(Injection.provideInvoiceRepository(sharedPreferences), Injection.provideProductsRepository());
        Category category = getIntent().getParcelableExtra(Category.class.getName());
        toolbar.setTitle(category.getName());

        presenter.retrieveCategoryProducts(category.getName(), this);
        initializeProductsRecyclerView();
    }

    private void initializeProductsRecyclerView() {
        RecyclerView productsRecyclerView = findViewById(R.id.products_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        productsRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(productsRecyclerView.getContext(),
                layoutManager.getOrientation());
        productsRecyclerView.addItemDecoration(dividerItemDecoration);

        productsAdapter = new ProductsAdapter(presenter);
        productsRecyclerView.setAdapter(productsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void proceedToCheckout(View view) {
        startActivity(new Intent(this, CheckoutActivity.class));
    }

    public void addToCart(View view) {
        Toast.makeText(this, "Items are added to cart successfully", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onRetrievingProductsSuccessfully(ArrayList<Product> products) {
        productsAdapter.bindProducts(products);
    }

    @Override
    public void onRetrievingProductsFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show();
    }
}
