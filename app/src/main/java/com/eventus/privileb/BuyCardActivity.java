package com.eventus.privileb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.eventus.privileb.Util.WSC;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;

import static com.eventus.privileb.General.Functionalities.isEmailValid;

public class BuyCardActivity extends Activity  {

    private EditText txtFirstName, txtLastName, txtEmail, txtMobileNumber, txtAddress, txtComments;
    private RadioButton  rbCOD, rbOnlinePayment;
    private Button btnActivate;
    private WSC _wsc;
    public FortCallBackManager fortCallback = null;
    /* public static final String TAG = "InAppBilling";
    IabHelper mHelper;
    public final String ITEM_SKU = "android.test.purchased"; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card);
        _wsc = new WSC();

        // String base64EncodedPublicKey = "<YOUR_KEY_HERE>";
        /* mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Log.d(TAG, "In-app billing setup failed: " + result);
                }
                else {
                    Log.d(TAG, "In-app billing is setup OK");
                }
            }
        });
        */
        txtFirstName = (EditText) findViewById(R.id.buycard_first_name);
        txtLastName = (EditText) findViewById(R.id.buycard_last_name);
        txtEmail = (EditText) findViewById(R.id.buycard_email);
        txtMobileNumber = (EditText) findViewById(R.id.buycard_mobile_number);
        txtAddress = (EditText) findViewById(R.id.buycard_address);
        txtComments = (EditText) findViewById(R.id.buycard_comments);
        rbCOD = (RadioButton) findViewById(R.id.buycard_COD);
        rbOnlinePayment = (RadioButton) findViewById(R.id.buycard_online_payment);

        ImageView btnBuyCardBack = (ImageView) findViewById(R.id.buycard_back);
        btnBuyCardBack.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        // initilizePayFortSDK();

        Button btnStartBuy = (Button) findViewById(R.id.btn_go_buy_privileb);
        btnStartBuy.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // requestForPayfortPayment();
                start_buying_card();
            }
        });
    }

    /*
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
    */

    public void start_buying_card(){
        if (txtFirstName.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buy Privileb Card");
            alertDialog.setMessage("First Name should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (txtLastName.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buy Privileb Card");
            alertDialog.setMessage("Last Name should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (txtEmail.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buy Privileb Card");
            alertDialog.setMessage("Email should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (isEmailValid(txtEmail.getText().toString().trim()) == false){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buy Privileb Card");
            alertDialog.setMessage("Email is not valid");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (txtMobileNumber.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buy Privileb Card");
            alertDialog.setMessage("Mobile number should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (txtAddress.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buy Privileb Card");
            alertDialog.setMessage("Address should not be empty");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        if (rbCOD.isChecked() == false && rbOnlinePayment.isChecked() == false){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Buy Privileb Card");
            alertDialog.setMessage("Payment method should be selected");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
            return;
        }
        String payment_method_id = "";
        if (rbCOD.isChecked() == true){
            payment_method_id = "1";
        }
        if (rbOnlinePayment.isChecked() == true){
            payment_method_id = "2";
        }

        _wsc.startBuyingCard(this, txtFirstName.getText().toString().trim(), txtLastName.getText().toString().trim(), txtEmail.getText().toString().trim(), txtMobileNumber.getText().toString().trim(), txtAddress.getText().toString().trim(), txtComments.getText().toString().trim(), payment_method_id);
    }

    /*
    public void buyClick(View view) {
        mHelper.launchPurchaseFlow(this, ITEM_SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(ITEM_SKU)) {
                // consumeItem();
                // buyButton.setEnabled(false);
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        // clickButton.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
    */

}
