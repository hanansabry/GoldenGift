package com.android.goldengift.backend.invoice;

import android.content.SharedPreferences;

import com.android.goldengift.model.Product;

public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final SharedPreferences sharedPreferences;

    public InvoiceRepositoryImpl(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void addNewProductItem(Product product) {
        orderItems.add(product);
    }

    @Override
    public void removeProductItem(Product product) {
        orderItems.remove(product);
    }

    @Override
    public double getProductItemTotalCost(Product product) {
        return product.getUnitPrice() * getProductItemQuantity(product);
    }

    @Override
    public void increaseProductItemQuantity(Product product) {
        int quantity = sharedPreferences.getInt(product.getId(), 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(product.getId(), ++quantity);
        editor.apply();

        //add this item to the final order request
        if (quantity > 0 && !orderItems.contains(product)) {
            addNewProductItem(product);
        }
    }

    @Override
    public void decreaseProductItemQuantity(Product product) {
        int quantity = getProductItemQuantity(product);
        if (quantity > 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(product.getId(), --quantity);
            editor.apply();
            if (quantity == 0) {
                removeProductItem(product);
            }
        } else {
            removeProductItem(product);
        }
    }

    @Override
    public int getProductItemQuantity(Product product) {
        return sharedPreferences.getInt(product.getId(), 0);
    }

    @Override
    public double getTotalCost() {
        double total = 0;
        for (Product product : orderItems) {
            total += product.getUnitPrice() * getProductItemQuantity(product);
        }
        return total;
    }
}
