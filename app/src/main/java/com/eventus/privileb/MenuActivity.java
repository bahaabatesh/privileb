package com.eventus.privileb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuActivity extends Activity {

    private Context context = this;
    private LinearLayout menuAboutPrivileb, menuAllDeals, menuNearbyMe, menuPrivilebCard, menuCategories, menuContactUs;
    private ImageView imageAboutPrivileb;
    private TextView textAboutPrivileb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.menu_register_btn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });
        RelativeLayout btnLogin = (RelativeLayout) findViewById(R.id.menu_login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_phonecall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this).create();
                alertDialog.setTitle("Call Charile Taxi");
                alertDialog.setMessage("1514");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
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
            }
        });

        RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.menu_hotline);
        btnCallHotLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this).create();
                alertDialog.setTitle("Call Hotline");
                alertDialog.setMessage("1514");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
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
            }
        });

    }

}

