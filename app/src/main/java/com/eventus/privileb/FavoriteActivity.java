package com.eventus.privileb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSCLogin;

public class FavoriteActivity extends Activity {

    ImageView btnBack1;
    private WSCLogin _wsc;
    private SharedPreferences prefs;
    private LinearLayout tabs[];
    private int selectTabIndex = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_login);
        _wsc = new WSCLogin();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        btnBack1 = (ImageView) findViewById(R.id.favorite_btn_back);
        btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final LinearLayout hintSearch = (LinearLayout)findViewById(R.id.search_hint_favorite);

        final EditText txtSearch = (EditText)findViewById(R.id.search_favorite);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintSearch.setVisibility(LinearLayout.GONE);
            }
        });

        final EditText editText = (EditText) findViewById(R.id.favorites_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    _wsc.displayFavorites(FavoriteActivity.this, (ViewGroup) findViewById(R.id.favorite_main), prefs.getLong(Enums.UserId, 0) + "",editText.getText().toString().trim());

                }
                return false;
            }
        });

        ImageView ivDeleteSearchText_categories = (ImageView) findViewById(R.id.filterscreen_cancel_search);
        ivDeleteSearchText_categories.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText("");
                    }
                }
        );

        tabs = new LinearLayout[] {
                (LinearLayout)findViewById(R.id.deals_tab_privileb),
                (LinearLayout)findViewById(R.id.deals_tab_nearby),
                (LinearLayout)findViewById(R.id.deals_tab_card),
                (LinearLayout)findViewById(R.id.deals_tab_categories),
                (LinearLayout)findViewById(R.id.deals_tab_favorite)
        };

        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setOnClickListener(tabClickListener2);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        EditText editText1 = (EditText) findViewById(R.id.favorites_search);
        _wsc.displayFavorites(FavoriteActivity.this, (ViewGroup) findViewById(R.id.favorite_main), prefs.getLong(Enums.UserId, 0) + "", editText1.getText().toString().trim());
    }

    View.OnClickListener tabClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tabs.length; i++) {
                if (v.getId() == tabs[i].getId()) {
                    // tabs[i].setAlpha(1.0f);
                    selectTabIndex = i;
                }
                else {
                    if (i != 2) {
                        // tabs[i].setAlpha(0.3f);
                    }
                }
            }
            showMenuContents2(selectTabIndex);
        }
    };

    void showMenuContents2(int index){
        switch (index) {
            case 0:
                Intent intent = new Intent(this, FeaturedDealsActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, LoginHomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "1");
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(this, LoginHomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, LoginHomeActivity.class);
                intent3.putExtra("SELECTED_INDEX_ID", "3");
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, FavoriteActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

}
