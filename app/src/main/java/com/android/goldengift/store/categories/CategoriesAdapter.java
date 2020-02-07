package com.android.goldengift.store.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.goldengift.R;
import com.android.goldengift.model.Category;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private final CategoriesPresenter presenter;
    private boolean gallery;

    public CategoriesAdapter(CategoriesPresenter presenter, boolean gallery) {
        this.presenter = presenter;
        this.gallery = gallery;
    }

    public void bindCategories(ArrayList<Category> categories) {
        presenter.bindCategories(categories);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (gallery) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_item_layout, parent, false);
            return new CategoryViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_item_layout, parent, false);
            return new CategoryViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        presenter.onBindCategoryAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getCategoriesSize();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryNameTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.category_name);
        }

        public void setCategoryName(Category category) {
            categoryNameTextView.setText(category.getName());
        }
    }
}
