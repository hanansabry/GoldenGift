package com.android.goldengift.backend.categories;

import com.android.goldengift.model.Category;

import java.util.ArrayList;

public interface CategoriesRepository {

    interface CategoriesRetrievingCallback {

        void onCategoriesRetrievedSuccessfully(ArrayList<Category> categories);

        void onCategoriesRetrievedFailed(String errmsg);
    }

    interface CategoriesInsertionCallback {

        void onInsertNewCategorySuccessfully();

        void onInsertNewCategoryFailed(String errmsg);
    }

    void retrieveCategoriesByCurrentStoreId(CategoriesRetrievingCallback callback);

    void retrieveAllCategories(CategoriesRetrievingCallback callback);

    void insertNewCategory(Category category, CategoriesInsertionCallback callback);
}
