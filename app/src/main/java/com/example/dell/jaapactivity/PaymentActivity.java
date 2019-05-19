package com.example.dell.jaapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class PaymentActivity extends AppCompatActivity {

    Button payWithPayPal ,payWithPaytm, payWithStripe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        payWithPayPal = findViewById(R.id.payWithPayPal);
        payWithPaytm = findViewById(R.id.payWithPayTm);
        payWithStripe = findViewById(R.id.payWithStripe);

        payWithStripe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this,StripePayment.class);
                startActivity(intent);
            }
        });
        payWithPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this,PayPalPayment.class);
                startActivity(intent);
            }
        });
        payWithPaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this,PaytmPayment.class);
                startActivity(intent);
            }
        });
    }
}
