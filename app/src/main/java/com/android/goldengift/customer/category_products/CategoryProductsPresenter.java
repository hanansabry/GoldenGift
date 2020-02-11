package com.android.goldengift.customer.category_products;

import com.android.goldengift.backend.invoice.InvoiceRepository;
import com.android.goldengift.backend.products.ProductsRepository;
import com.android.goldengift.model.Product;

import java.util.ArrayList;

public class CategoryProductsPresenter {

    private ArrayList<Product> products = new ArrayList<>();
    private final InvoiceRepository invoiceRepository;
    private final ProductsRepository productsRepository;

    public CategoryProductsPresenter(InvoiceRepository invoiceRepository, ProductsRepository productsRepository) {
        this.invoiceRepository = invoiceRepository;
        this.productsRepository = productsRepository;
    }

    public void bindProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void onBindProductAtPosition(ProductsAdapter.ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.setProductImage(product.getImagesUrls() != null ? product.getImagesUrls().get(0) : null);
        holder.setProductName(product.getName());
        holder.setUnitPrice(product.getUnitPrice());
        holder.setProductItemTotalCost(invoiceRepository.getProductItemTotalCost(product));
        holder.setQuantity(invoiceRepository.getProductItemQuantity(product));
    }

    public int getProductsSize() {
        return products.size();
    }

    public void increaseQuantity(ProductsAdapter.ProductViewHolder holder, int adapterPosition) {
        Product product = products.get(adapterPosition);
        invoiceRepository.increaseProductItemQuantity(product);
        holder.setQuantity(invoiceRepository.getProductItemQuantity(product));
        holder.setProductItemTotalCost(invoiceRepository.getProductItemTotalCost(product));
    }

    public void decreaseQuantity(ProductsAdapter.ProductViewHolder holder, int adapterPosition) {
        Product product = products.get(adapterPosition);
        invoiceRepository.decreaseProductItemQuantity(product);
        holder.setQuantity(invoiceRepository.getProductItemQuantity(product));
        holder.setProductItemTotalCost(invoiceRepository.getProductItemTotalCost(product));
    }

    public void retrieveCategoryProducts(String categoryName, ProductsRepository.ProductsRetrievingCallback callback) {
        productsRepository.retrieveProductsByCategoryId(categoryName, callback);
    }

    public double getOrderTotalCost() {
        return invoiceRepository.getTotalCost();
    }
}
