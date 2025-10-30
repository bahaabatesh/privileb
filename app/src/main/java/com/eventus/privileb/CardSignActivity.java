package com.eventus.privileb;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.navdrawer.SimpleSideDrawer;

/**
 * Created by forever on 5/10/17.
 */

public class CardSignActivity extends Activity {

    private Context context = this;
    private SimpleSideDrawer mNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardsignout);

        mNav = new SimpleSideDrawer(this);
        mNav.setLeftBehindContentView(R.layout.activity_menu);

        Button btnBuycard = (Button) findViewById(R.id.btn_buy_privileb_card);
        btnBuycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
