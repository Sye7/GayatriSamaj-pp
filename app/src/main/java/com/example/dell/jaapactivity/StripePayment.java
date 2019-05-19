package com.example.dell.jaapactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Product;

import java.util.HashMap;
import java.util.Map;

public class StripePayment extends AppCompatActivity {

    Button save ;
    Context mContext;
    CardMultilineWidget mCardInputWidget;

    ProgressBar progressBar;
    public static final String PUBLISHABLE_KEY = "pk_test_k8Yg7GFqkwXt8Onk4qrXQaLx";
    public static final String SECRET_KEY = "sk_test_lmpdZG8GGskif1mhATFoWlls";
    private static final String TAG = "StripePayment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        progressBar = findViewById(R.id.progress_bar);
        mCardInputWidget = (CardMultilineWidget) findViewById(R.id.card_multiline_widget);
        final Card cardToSave = mCardInputWidget.getCard();
        if (cardToSave == null) {
            Toast.makeText(StripePayment.this,"Invalid card",Toast.LENGTH_SHORT).show();
        }
        mContext = this;
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mCardInputWidget.getCard()!= null) {
                    progressBar.setVisibility(View.VISIBLE);
                    String number = mCardInputWidget.getCard().getNumber();
                    Log.d(TAG, "onClick: number " + number);
                    Integer expiryMonth = mCardInputWidget.getCard().getExpMonth();
                    Integer expiryYear = mCardInputWidget.getCard().getExpYear();
                    String cvc = mCardInputWidget.getCard().getCVC();
                    Log.d(TAG, "onClick: expiry month " + expiryMonth + "expiry year " + expiryYear
                            + "cvc " + cvc);

                    Card card = new Card(number,expiryMonth,expiryYear,cvc);

                    Stripe stripe  = new Stripe(mContext,PUBLISHABLE_KEY);

                        stripe.createToken(card, new TokenCallback() {
                            @Override
                            public void onError(Exception error) {
                                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                            }


                            @Override
                            public void onSuccess(Token token) {
                                Log.d(TAG, "onSuccess: Token " + token);
                                Toast.makeText(StripePayment.this, "Done", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                createProduct();

                            }
                        });
                    }


                }

        });





    }

    private void createProduct() {

        Stripe stripe = new Stripe(getApplication(),PUBLISHABLE_KEY);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "My SaaS Platform");
        params.put("type", "service");

        try {
            Product.create(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "createProduct: Product created "+params);
        createPlan();


    }

    private void createPlan() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("product", "prod_CbvTFuXWh7BPJH");
        params.put("nickname", "SaaS Platform USD");
        params.put("interval", "month");
        params.put("currency", "usd");
        params.put("amount", 10000);
        try {
            Plan plan = Plan.create(params);
            Log.d(TAG, "createPlan: "+plan);

        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }

        createCustomer();
    }

    private void createCustomer() {
        Map<String, Object> params = new HashMap<>();
        params.put("email", "jenny.rosen@example.com");
        params.put("source", "src_18eYalAHEMiOZZp1l9ZTjSU0");
        try {
            Customer customer = Customer.create(params);
            Log.d(TAG, "createCustomer: "+customer);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (CardException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        }

        subscribeTheCustomerToThePlan();
    }

    private void subscribeTheCustomerToThePlan() {
        Map<String, Object> item = new HashMap<>();
        item.put("plan", "plan_CBXbz9i7AIOTzr");
        Map<String, Object> items = new HashMap<>();
        items.put("0", item);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", "cus_4fdAW5ftNQow1a");
        params.put("items", items);

    }


}
