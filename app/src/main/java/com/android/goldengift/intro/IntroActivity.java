package com.android.goldengift.intro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.goldengift.R;
import com.android.goldengift.customer.GalleryActivity;
import com.android.goldengift.login.LoginActivity;
import com.android.goldengift.register.RegisterActivity;
import com.android.goldengift.store.StoreHome;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;

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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent mainIntent = new Intent(this, StoreHome.class);
            startActivity(mainIntent);
        } else {
            Intent mainIntent = new Intent(this, LoginActivity.class);
            startActivity(mainIntent);
        }
    }

    public void goToGalleryScreen(View view) {
        startActivity(new Intent(this, GalleryActivity.class));
    }
}
