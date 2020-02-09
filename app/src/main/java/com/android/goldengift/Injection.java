package com.android.goldengift;

import com.android.goldengift.backend.authentication.AuthenticationRepository;
import com.android.goldengift.backend.authentication.AuthenticationRepositoryImpl;
import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.backend.categories.CategoriesRepositoryImpl;
import com.android.goldengift.backend.products.ProductsRepository;
import com.android.goldengift.backend.products.ProductsRepositoryImpl;
import com.android.goldengift.backend.storage.ImagesStorage;
import com.android.goldengift.backend.storage.ImagesStorageImpl;
import com.android.goldengift.backend.unitprice.UnitPriceRepository;
import com.android.goldengift.backend.unitprice.UnitPriceRepositoryImpl;
import com.android.goldengift.store.products.AddProductUseCaseHandler;

public class Injection {

    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }

    public static CategoriesRepository provideCategoriesRepository() {
        return new CategoriesRepositoryImpl();
    }

    public static AddProductUseCaseHandler provideAddProductUseCaseHandler() {
        return new AddProductUseCaseHandler(provideCategoriesRepository(), provideProductsRepository(), provideUnitPriceRepository());
    }

    private static ProductsRepository provideProductsRepository() {
        return new ProductsRepositoryImpl(provideImagesStorage());
    }

    private static ImagesStorage provideImagesStorage() {
        return new ImagesStorageImpl();
    }

    private static UnitPriceRepository provideUnitPriceRepository() {
        return new UnitPriceRepositoryImpl();
    }
}
