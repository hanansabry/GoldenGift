package com.android.goldengift.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.goldengift.EmptyRecyclerView;
import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.customer.category_products.CategoryProductsActivity;
import com.android.goldengift.model.Category;
import com.android.goldengift.store.categories.CategoriesAdapter;
import com.android.goldengift.store.categories.CategoriesPresenter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

public class CustomerCategoriesActivity extends AppCompatActivity implements CategoriesRepository.CategoriesRetrievingCallback, CategoriesPresenter.CategoryClickListener {

    private CategoriesPresenter presenter;
    private CategoriesAdapter mCategoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new CategoriesPresenter(Injection.provideCategoriesRepository());
        initializeCategoriesRecylerView();
        presenter.retrieveAllCategories(this);
    }

    private void initializeCategoriesRecylerView() {
        mCategoriesAdapter = new CategoriesAdapter(presenter, true, this);

        EmptyRecyclerView mCategoriesRecyclerView = findViewById(R.id.categories_recyclerView);
        mCategoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mCategoriesRecyclerView.setAdapter(mCategoriesAdapter);
        mCategoriesRecyclerView.setEmptyView(findViewById(R.id.empty_view));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCategoriesRetrievedSuccessfully(ArrayList<Category> categories) {
        mCategoriesAdapter.bindCategories(categories);
    }

    @Override
    public void onCategoriesRetrievedFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClicked(int position) {
        Category category = presenter.getCategoryAtPosition(position);
        Intent intent = new Intent(this, CategoryProductsActivity.class);
        intent.putExtra(Category.class.getName(), category);
        startActivity(intent);
    }
}
