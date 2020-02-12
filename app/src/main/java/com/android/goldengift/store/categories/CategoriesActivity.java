package com.android.goldengift.store.categories;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.goldengift.EmptyRecyclerView;
import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.model.Category;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CategoriesActivity extends AppCompatActivity implements CategoriesRepository.CategoriesRetrievingCallback, CategoriesRepository.CategoriesInsertionCallback {

    private CategoriesPresenter presenter;
    private CategoriesAdapter mCategoriesAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new CategoriesPresenter(Injection.provideCategoriesRepository());
        initializeCategoriesRecylerView();
        presenter.retrieveAllCategories(this);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initializeCategoriesRecylerView() {
        mCategoriesAdapter = new CategoriesAdapter(presenter, false, null);

        EmptyRecyclerView mCategoriesRecyclerView = findViewById(R.id.categories_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mCategoriesRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mCategoriesRecyclerView.getContext(),
                layoutManager.getOrientation());
        mCategoriesRecyclerView.addItemDecoration(dividerItemDecoration);
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

    public void addNewCategory(View view) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.add_category_input_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText categoryNameEditText = mView.findViewById(R.id.category_name_edittext);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Category category = new Category();
                        category.setName(categoryNameEditText.getText().toString().trim());
                        presenter.addNewCategory(category, CategoriesActivity.this);
                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Override
    public void onCategoriesRetrievedSuccessfully(ArrayList<Category> categories) {
        mCategoriesAdapter.bindCategories(categories);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onCategoriesRetrievedFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onInsertNewCategorySuccessfully() {
        Toast.makeText(this, "New Category is added successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInsertNewCategoryFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }
}
