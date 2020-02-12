package com.android.goldengift.customer.payment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.android.goldengift.R;
import com.android.goldengift.intro.IntroActivity;
import com.android.goldengift.model.Order;

import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PaymentActivity extends AppCompatActivity {

    private EditText dateTimeEditText;
    private String selectedPaymentMethod = Order.PaymentMethod.OnDelivery.name();
    private Order order;
    private String dateAndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        order = getIntent().getParcelableExtra(Order.class.getName());
        order.setPaymentMethod(selectedPaymentMethod);

        initializeDateTimePicker();
        initializePaymentMethodRadioButtons();
    }

    private void initializeDateTimePicker() {
        dateTimeEditText = findViewById(R.id.date_time_edit_text);
        dateTimeEditText.setInputType(InputType.TYPE_NULL);
        dateTimeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                dateTimeEditText.setError(null);
            }
        });
        dateTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupCalenderPickerDialog(dateTimeEditText);
            }
        });
    }

    private void setupCalenderPickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        //date picker dialog
        DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day = String.format(Locale.US, "%1d/%2d/%3d", dayOfMonth, month + 1, year);
                setupTimePickerDialog(day, editText);
            }
        }, year, month, day);
        pickerDialog.show();
    }

    private void setupTimePickerDialog(final String day, final EditText editText) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        dateAndTime = String.format("%s, %d:%d", day, hourOfDay, minute);
                        editText.setText(dateAndTime);
                        order.setDeliveryTime(dateAndTime);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void initializePaymentMethodRadioButtons() {
        RadioGroup paymentRadioGroup = findViewById(R.id.payment_group);
        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.pay_on_deliver_radiobutton) {
                    selectedPaymentMethod = Order.PaymentMethod.OnDelivery.name();
                } else if (checkedId == R.id.pay_online_radiobutton) {
                    selectedPaymentMethod = Order.PaymentMethod.Online.name();
                }
                order.setPaymentMethod(selectedPaymentMethod);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, IntroActivity.class));
    }

    public void onNextClicked(View view) {
        if (dateAndTime == null || dateAndTime.isEmpty()) {
            dateTimeEditText.setError("You should schedule a date");
        } else {
            Intent intent = null;
            if (Order.PaymentMethod.valueOf(selectedPaymentMethod) == Order.PaymentMethod.Online) {
                intent = new Intent(this, OnlinePaymentActivity.class);
            } else if (Order.PaymentMethod.valueOf(selectedPaymentMethod) == Order.PaymentMethod.OnDelivery) {
                intent = new Intent(this, OnDeliveryActivity.class);
            }
            intent.putExtra(Order.class.getName(), order);
            startActivity(intent);
        }
    }
}
