package com.android.goldengift.backend.products;

import com.android.goldengift.model.Product;

import java.util.ArrayList;

public interface ProductsRepository {

    interface ProductsRetrievingCallback {

        void onRetrievingProductsSuccessfully(ArrayList<Product> proudcts);

        void onRetrievingProductsFailed(String errmsg);
    }

    interface AddProductsCallback {

        void onAddingNewProductsSuccessfully();

        void onAddingNewProductFailed(String errmsg);
    }

    void retrieveProductsByCategoryId(String categoryId, ProductsRetrievingCallback callback);

    void addNewProduct(Product product, AddProductsCallback callback);
}
