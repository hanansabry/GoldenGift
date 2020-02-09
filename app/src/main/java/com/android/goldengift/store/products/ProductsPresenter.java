package com.android.goldengift.store.products;

import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.backend.products.ProductsRepository;
import com.android.goldengift.backend.unitprice.UnitPriceRepository;
import com.android.goldengift.model.Product;

public class ProductsPresenter {
    
    private final ProductsActivity view;
    private final AddProductUseCaseHandler addProductUseCaseHandler;

    public ProductsPresenter(ProductsActivity view, AddProductUseCaseHandler addProductUseCaseHandler) {
        this.view = view;
        this.addProductUseCaseHandler = addProductUseCaseHandler;
    }

    private boolean validateModelData(Product product) {
        boolean isValid = true;
        if (product.getCategoryId() == null) {
            isValid = false;
            view.setSelectCategoryErrorMessage();
        }
        if (product.getName() == null || product.getName().isEmpty()) {
            isValid = false;
            view.setProductNameErrorMessage();
        }

        if (product.getUnit() == null || product.getUnit().isEmpty()) {
            isValid = false;
            view.setUnitErrorMessage();
        }
        if (product.getUnitPrice() == 0) {
            isValid = false;
            view.setUnitPriceErrorMessage();
        }

        if (product.getImagesUrls() == null || product.getImagesUrls().size() == 0) {
            isValid = false;
            view.setProductImagesErrorMessage();
        }
        return isValid;
    }

    public void addNewProduct(Product product, ProductsRepository.AddProductsCallback callback) {
        if (validateModelData(product)) {
            addProductUseCaseHandler.addNewProduct(product, callback);
        }
    }

    public void retrieveCategories(CategoriesRepository.CategoriesRetrievingCallback callback) {
        addProductUseCaseHandler.retrieveStoreCategories(callback);
    }

    public void retrieveUnitPrice(UnitPriceRepository.UnitPriceRetrievingCallback callback) {
        addProductUseCaseHandler.retrieveUnitPrices(callback);
    }
}
