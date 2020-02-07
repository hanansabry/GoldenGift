package com.android.goldengift.intro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.goldengift.R;
import com.android.goldengift.login.LoginActivity;
import com.android.goldengift.register.RegisterActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    public void onStoreRegisterClicked(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onStoreLoginClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void goToGalleryScreen(View view) {
    }
}
