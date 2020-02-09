package com.android.goldengift.store.products;

import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.backend.products.ProductsRepository;
import com.android.goldengift.backend.unitprice.UnitPriceRepository;
import com.android.goldengift.model.Product;

public class AddProductUseCaseHandler {

    private final CategoriesRepository categoriesRepository;
    private final ProductsRepository productsRepository;
    private final UnitPriceRepository unitPriceRepository;

    public AddProductUseCaseHandler(CategoriesRepository categoriesRepository, ProductsRepository productsRepository, UnitPriceRepository unitPriceRepository) {
        this.categoriesRepository = categoriesRepository;
        this.productsRepository = productsRepository;
        this.unitPriceRepository = unitPriceRepository;
    }

    public void addNewProduct(Product product, ProductsRepository.AddProductsCallback callback) {
        productsRepository.addNewProduct(product, callback);
    }

    public void retrieveStoreCategories(CategoriesRepository.CategoriesRetrievingCallback callback) {
        categoriesRepository.retrieveCategoriesByCurrentStoreId(callback);
    }

    public void retrieveUnitPrices(UnitPriceRepository.UnitPriceRetrievingCallback callback) {
        unitPriceRepository.retrieveUnitPrices(callback);
    }
}
