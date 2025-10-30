package com.eventus.privileb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuLoginActivity extends AppCompatActivity {

    private Context context = this;
    private LinearLayout menuAboutPrivileb, menuAllDeals, menuNearbyMe, menuPrivilebCard, menuCategories, menuFavorites, menuContactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_login);

        RelativeLayout btnphone = (RelativeLayout) findViewById(R.id.menu_login_phone);
        btnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(MenuLoginActivity.this).create();
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

        TextView viewSignout = (TextView)findViewById(R.id.btn_signout);
        viewSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}

