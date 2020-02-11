package com.android.goldengift.customer.payment;

import android.content.Intent;
import android.os.Bundle;

import com.android.goldengift.R;
import com.android.goldengift.intro.IntroActivity;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, IntroActivity.class));
    }
}
