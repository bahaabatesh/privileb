package com.eventus.privileb;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.eventus.privileb.Util.WSC;

import static com.eventus.privileb.General.Functionalities.isEmailValid;

public class SignupActivity extends AppCompatActivity {

    WSC _wsc;
    EditText txtName, txtEmail, txtMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button btnSignup = (Button)findViewById(R.id.login_btn_signup);
        btnSignup.setOnClickListener(btnClickListener);
        Button btnSkip = (Button) findViewById(R.id.login_btn_skip);
        txtName = (EditText) findViewById(R.id.signup_name);
        txtEmail = (EditText) findViewById(R.id.signup_email);
        txtMobileNumber = (EditText) findViewById(R.id.signup_mobile_number);
        btnSkip.setOnClickListener(btnClickListener);
        _wsc = new WSC();
    }

    View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.login_btn_signup:
                    sign_up();
                    break;
                case R.id.login_btn_skip:
                    intent = new Intent(SignupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    public void sign_up(){
        if (txtName.getText().toString().trim().equals("")){
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Sign up");
            alertDialog.setMessage("Name should not be empty");
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
            alertDialog.setTitle("Sign up");
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
            alertDialog.setTitle("Sign up");
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
            alertDialog.setTitle("Sign up");
            alertDialog.setMessage("Mobile should not be empty");
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
        _wsc.signUp(this, txtName.getText().toString().trim(), txtEmail.getText().toString().trim(), txtMobileNumber.getText().toString().trim());
    }

}
