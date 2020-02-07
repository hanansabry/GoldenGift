package com.android.goldengift.login;

import com.android.goldengift.backend.authentication.AuthenticationRepository;

public class LoginPresenter {

    private final LoginActivity view;
    private final AuthenticationRepository authenticationRepository;


    public LoginPresenter(LoginActivity view, AuthenticationRepository authenticationRepository) {
        this.view = view;
        this.authenticationRepository = authenticationRepository;
    }

    public void login(String email, String password, AuthenticationRepository.LoginCallback callback) {
        authenticationRepository.login(email, password, callback);
    }

    public boolean validateLoginData(String email, String password) {
        boolean validate = true;
        if (email == null || email.isEmpty()) {
            validate = false;
            view.setEmailInputTextErrorMessage();
        }

        if (password == null || password.isEmpty()) {
            validate = false;
            view.setPasswordInputTextErrorMessage();
        }
        return validate;
    }
}
