package com.android.goldengift.backend.authentication;

import com.android.goldengift.model.Store;
import com.google.firebase.auth.FirebaseUser;

public interface AuthenticationRepository {

    interface RegistrationCallback {
        void onSuccessfulRegistration(FirebaseUser firebaseUser);

        void onFailedRegistration(String errmsg);
    }

    interface LoginCallback {
        void onSuccessLogin(FirebaseUser firebaseUser);

        void onFailedLogin(String errmsg);
    }

    void registerNewStore(Store user, RegistrationCallback callback);

    void login(String email, String password, LoginCallback callback);

}
