package com.example.dell.jaapactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PaytmPayment extends AppCompatActivity {

    EditText orderID,customerID;
    Button pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paytm_payment);

        orderID = findViewById(R.id.orderId);
        customerID = findViewById(R.id.customerID);

        pay = findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(PaytmPayment.this,Checksum.class);
                in.putExtra("orderid",orderID.getText().toString());
                in.putExtra("custid",customerID.getText().toString());
                startActivity(in);

            }
        });
    }
}
