package com.android.goldengift;

import com.android.goldengift.backend.authentication.AuthenticationRepository;
import com.android.goldengift.backend.authentication.AuthenticationRepositoryImpl;
import com.android.goldengift.backend.categories.CategoriesRepository;
import com.android.goldengift.backend.categories.CategoriesRepositoryImpl;

public class Injection {

    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }

    public static CategoriesRepository provideCategoriesRepository() {
        return new CategoriesRepositoryImpl();
    }
}
