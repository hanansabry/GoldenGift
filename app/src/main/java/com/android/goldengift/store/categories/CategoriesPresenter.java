package com.android.goldengift.store.categories;

import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.model.Category;

import java.util.ArrayList;

public class CategoriesPresenter {

    private final CategoriesRepository categoriesRepository;
    private ArrayList<Category> categories = new ArrayList<>();

    public CategoriesPresenter(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public void addNewCategory(Category category, CategoriesRepository.CategoriesInsertionCallback callback) {
        categoriesRepository.insertNewCategory(category, callback);
    }

    public void retrieveAllCategories(CategoriesRepository.CategoriesRetrievingCallback callback) {
        categoriesRepository.retrieveAllCategories(callback);
    }

    public void bindCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public int getCategoriesSize() {
        return categories.size();
    }

    public void onBindCategoryAtPosition(CategoriesAdapter.CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.setCategoryName(category);
    }
}
