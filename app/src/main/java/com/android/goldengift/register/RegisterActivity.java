package com.android.goldengift.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.goldengift.Injection;
import com.android.goldengift.R;
import com.android.goldengift.backend.authentication.AuthenticationRepository;
import com.android.goldengift.login.LoginActivity;
import com.android.goldengift.model.Store;
import com.android.goldengift.store.StoreHome;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements AuthenticationRepository.RegistrationCallback {

    private RegisterPresenter mPresenter;
    private TextInputLayout nameTextInput, userNameTextInput, passwordTextInput, emailTextInput,phoneTextInput;
    private EditText nameEditText, userNameEditText, passwordEditText, emailEditText, phoneEditText;
    private ProgressBar progressBar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPresenter = new RegisterPresenter(this, Injection.provideAuthenticationRepository());
        initializeView();
    }

    private void initializeView() {
        nameTextInput = findViewById(R.id.name_text_input_edittext);
        userNameTextInput = findViewById(R.id.username_text_input_edittext);
        passwordTextInput = findViewById(R.id.password_text_input_edittext);
        emailTextInput = findViewById(R.id.email_text_input_edittext);
        phoneTextInput = findViewById(R.id.phone_text_input_edittext);

        nameEditText = findViewById(R.id.name_edit_text);
        nameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                nameTextInput.setError(null);
                nameTextInput.setErrorEnabled(false);
            }
        });
        userNameEditText = findViewById(R.id.username_edit_text);
        userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                userNameTextInput.setError(null);
                userNameTextInput.setErrorEnabled(false);
            }
        });
        passwordEditText = findViewById(R.id.password_edit_text);
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                passwordTextInput.setError(null);
                passwordTextInput.setErrorEnabled(false);
            }
        });
        emailEditText = findViewById(R.id.email_edit_text);
        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                emailTextInput.setError(null);
                emailTextInput.setErrorEnabled(false);
            }
        });
        phoneEditText = findViewById(R.id.phone_edit_text);
        phoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phoneTextInput.setError(null);
                phoneTextInput.setErrorEnabled(false);
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        registerButton = findViewById(R.id.register);
    }

    private Store getUserInputData() {
        Store user = new Store();
        user.setName(nameEditText.getText().toString().trim());
        user.setUserName(userNameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString().trim());
        user.setEmail(emailEditText.getText().toString().trim());
        user.setPhone(phoneEditText.getText().toString().trim());
        return user;
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    public void onLoginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void setNameInputTextErrorMessage() {
        nameTextInput.setError("Name can't be empty ");
    }

    public void setUserNameInputTextErrorMessage() {
        userNameTextInput.setError("Username can't be empty");
    }

    public void setPasswordInputTextErrorMessage(String s) {
        passwordTextInput.setError(s);
    }

    public void setEmailInputTextErrorMessage() {
        emailTextInput.setError("Email can't be empty");
    }

    public void setPhoneInputTextErrorMessage() {
        phoneTextInput.setError("Phone can't be empty");
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
    }

    private void goToStorePage() {
        Intent homeIntent = new Intent(this, StoreHome.class);
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }

    public void OnRegisterClicked(View view) {
        showProgressBar();
        Store user = getUserInputData();
        if (mPresenter.validateStoreData(user)) {
            mPresenter.registerNewUser(user, this);
        } else {
            hideProgressBar();
        }
    }

    @Override
    public void onSuccessfulRegistration(FirebaseUser firebaseUser) {
        hideProgressBar();
        Toast.makeText(this, "Registration is successfully completed\n Welcome " + firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
        goToStorePage();
    }

    @Override
    public void onFailedRegistration(String errmsg) {
        hideProgressBar();
        Toast.makeText(this, "Registration Failed\n" + errmsg, Toast.LENGTH_LONG).show();
    }
}
