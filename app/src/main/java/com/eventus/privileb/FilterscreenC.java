package com.eventus.privileb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSCLogin;

public class FilterscreenC extends AppCompatActivity {

    private SharedPreferences prefs;
    private WSCLogin _wsc;
    private LinearLayout btnFilter;
    private ImageButton btnMenu;
    EditText editText;
    private LinearLayout tabs[];
    private int selectTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterscreen_menu);
        _wsc = new WSCLogin();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        btnFilter = (LinearLayout) findViewById(R.id.login_btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterscreenC.this, FilterActivity.class);
                startActivity(intent);
            }
        });

        btnMenu = (ImageButton) findViewById(R.id.deals_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final WSCLogin _wsclogin9 = new WSCLogin();
        _wsclogin9.getUserFavoritesFeaturedDeals(this, (ViewGroup) findViewById(R.id.filterscreen_main), prefs.getLong(Enums.UserId, 0) + "", "filter_by_categories", "");

        final EditText editText_categories = (EditText) findViewById(R.id.filterscreen_search);
        editText_categories.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    _wsc.getUserFavoritesFeaturedDeals(FilterscreenC.this, (ViewGroup) findViewById(R.id.filterscreen_main), prefs.getLong(Enums.UserId, 0) + "", "filterscreen_categories",editText_categories.getText().toString().trim());
                }
                return false;
            }
        });

        ImageView ivDeleteSearchText_categories = (ImageView) findViewById(R.id.filterscreen_cancel_search);
        ivDeleteSearchText_categories.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText_categories.setText("");
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
        EditText editText1 = (EditText) findViewById(R.id.filterscreen_search);
        _wsc.getUserFavoritesFeaturedDeals(FilterscreenC.this, (ViewGroup) findViewById(R.id.filterscreen_main), prefs.getLong(Enums.UserId, 0) + "", "filterscreen_categories", editText1.getText().toString().trim());
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
