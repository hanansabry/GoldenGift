package com.android.goldengift.backend.invoice;

import com.android.goldengift.model.Product;

import java.util.ArrayList;

public interface InvoiceRepository {

    ArrayList<Product> orderItems = new ArrayList<>();

    void addNewProductItem(Product product);

    void removeProductItem(Product product);

    double getProductItemTotalCost(Product product);

    void increaseProductItemQuantity(Product product);

    void decreaseProductItemQuantity(Product product);

    int getProductItemQuantity(Product product);

    double getTotalCost();

}
