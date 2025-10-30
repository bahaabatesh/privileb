package com.eventus.privileb;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.navdrawer.SimpleSideDrawer;
import com.eventus.privileb.Util.WSC;

/**
 * Created by BLOOMAY on 7/12/2017.
 */

public class CharitiesActivity extends AppCompatActivity {

    WSC _wsc = new WSC();
    private Context context = this;
    private SimpleSideDrawer mNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charities);


        _wsc.loadCharities(this, (ViewGroup) findViewById(R.id.charities_main));

        RelativeLayout rlTitle = (RelativeLayout) findViewById(R.id.charities_title);
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ImageView ivCharities = (ImageView) findViewById(R.id.charities_back);
        ivCharities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
