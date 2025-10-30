package com.eventus.privileb;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eventus.privileb.Util.IPaymentRequestCallBack;
import com.eventus.privileb.Util.PayFortData;
import com.eventus.privileb.Util.PayFortPayment;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;

import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
/**
 * Created by BLOOMAY on 6/22/2017.
 */

public class CCPaymentActivity extends AppCompatActivity implements IPaymentRequestCallBack, View.OnClickListener {

    public FortCallBackManager fortCallback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cc_payment);

        Button btnStartBuy = (Button) findViewById(R.id.payBtn);
        btnStartBuy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // requestForPayfortPayment();
            }
        });

        Button btnBack = (Button) findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
               finish();
            }
        });
    }

    private void initilizePayFortSDK() {
        fortCallback = FortCallback.Factory.create();
    }

    private void requestForPayfortPayment() {
        PayFortData payFortData = new PayFortData();

        payFortData.amount = "111"; // Multiplying with 100, bcz amount should not be in decimal format
        payFortData.command = PayFortPayment.PURCHASE;
        payFortData.currency = PayFortPayment.CURRENCY_TYPE;
        payFortData.customerEmail = "readyandroid@gmail.com";
        payFortData.language = PayFortPayment.LANGUAGE_TYPE;
        payFortData.merchantReference = String.valueOf(System.currentTimeMillis());
        payFortData.cardNumber = "";
        payFortData.paymentOption = "";

        PayFortPayment payFortPayment = new PayFortPayment(this, this.fortCallback, this);
        payFortPayment.requestForPayment(payFortData);

    }

    @Override
    public void onPaymentRequestResponse(int responseType, final PayFortData responseData) {
        if (responseType == PayFortPayment.RESPONSE_GET_TOKEN) {
            Toast.makeText(this, "Token not generated", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Token not generated");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_CANCEL) {
            Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment cancelled");
        } else if (responseType == PayFortPayment.RESPONSE_PURCHASE_FAILURE) {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment failed");
        } else {
            Toast.makeText(this, "Payment successful", Toast.LENGTH_SHORT).show();
            Log.e("onPaymentResponse", "Payment successful");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_buy_privileb:
                requestForPayfortPayment();
                break;
        }
    }

}
