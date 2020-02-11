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
    private final CategoriesPresenter.CategoryClickListener categoryClickListener;

    public CategoriesAdapter(CategoriesPresenter presenter, boolean gallery, CategoriesPresenter.CategoryClickListener categoryClickListener) {
        this.presenter = presenter;
        this.gallery = gallery;
        this.categoryClickListener = categoryClickListener;
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
            if (gallery) {
                View cardView = itemView.findViewById(R.id.category_layout);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (categoryClickListener != null) {
                            categoryClickListener.onCategoryClicked(getAdapterPosition());
                        }
                    }
                });
            }
        }

        public void setCategoryName(Category category) {
            categoryNameTextView.setText(category.getName());
        }
    }
}
