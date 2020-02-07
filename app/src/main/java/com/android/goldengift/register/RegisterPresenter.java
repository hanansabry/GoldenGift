package com.android.goldengift.register;

import com.android.goldengift.backend.authentication.AuthenticationRepository;
import com.android.goldengift.model.Store;

public class RegisterPresenter {

    private final RegisterActivity view;
    private final AuthenticationRepository authenticationRepository;

    public RegisterPresenter(RegisterActivity view, AuthenticationRepository authenticationRepository) {
        this.view = view;
        this.authenticationRepository = authenticationRepository;
    }

    public void registerNewUser(Store store, AuthenticationRepository.RegistrationCallback callback) {
        authenticationRepository.registerNewStore(store, callback);
    }

    public boolean validateStoreData(Store user) {
        boolean validate = true;
        if (user.getName() == null || user.getName().isEmpty()) {
            validate = false;
            view.setNameInputTextErrorMessage();
        }

        if (user.getUserName() == null || user.getUserName().isEmpty()) {
            validate = false;
            view.setUserNameInputTextErrorMessage();
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            validate = false;
            view.setPasswordInputTextErrorMessage("Password can't be empty");
        }

        if (user.getPassword().length() < 6) {
            validate = false;
            view.setPasswordInputTextErrorMessage("Password must be at least 6 characters");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            validate = false;
            view.setEmailInputTextErrorMessage();
        }

        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            validate = false;
            view.setPhoneInputTextErrorMessage();
        }
        return validate;
    }

}
