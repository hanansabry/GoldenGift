package com.android.goldengift;

import com.android.goldengift.backend.authentication.AuthenticationRepository;
import com.android.goldengift.backend.authentication.AuthenticationRepositoryImpl;

public class Injection {

    public static AuthenticationRepository provideAuthenticationRepository() {
        return new AuthenticationRepositoryImpl();
    }
}
