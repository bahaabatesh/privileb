package com.eventus.privileb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSC;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.eventus.privileb.Util.WSCLogin;

/**
 * Created by BLOOMAY on 7/10/2017.
 */

public class ScannerActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    WSC _wsc;
    WSCLogin _wsclogin;
    EditText et1, et2, et3, et4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        sharedpreferences = getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
        _wsc = new WSC();
        _wsclogin = new WSCLogin();

        Button btnScanQrCode = (Button) findViewById(R.id.scanner_scanqrcode);
        btnScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(ScannerActivity.this).initiateScan();
            }
        });

        TextView txtLogout = (TextView) findViewById(R.id.scanner_logout);
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _wsclogin.logout(ScannerActivity.this);
            }
        });

        et1 = (EditText) findViewById(R.id.scanner_code1);
        et2 = (EditText) findViewById(R.id.scanner_code2);
        et3 = (EditText) findViewById(R.id.scanner_code3);
        et4 = (EditText) findViewById(R.id.scanner_code4);

        _wsc.loadScanner(this, (ViewGroup) findViewById(R.id.scanner_main));

        et1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et1.length() == 1){
                    et2.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });

        et2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et2.length() == 1){
                    et3.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });

        et3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(et3.length() == 1){
                    et4.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }
        });
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                // Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Successfully Scanned", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Enums.ScannerSerialNumber, result.getContents());
                editor.commit();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            _wsclogin.logout(ScannerActivity.this);
            //finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
