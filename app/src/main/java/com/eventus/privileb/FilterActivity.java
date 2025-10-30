package com.eventus.privileb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSC;

/**
 * Created by forever on 5/8/17.
 */


public class FilterActivity extends Activity {

    private RelativeLayout filterLine;
    private RelativeLayout filterByCategory;
    LayoutInflater inflater;
    private Context context = this;
    WSC _wsc;
    private SharedPreferences prefs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        _wsc = new WSC();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);



        filterLine = (RelativeLayout) findViewById(R.id.filter_location);
        filterLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LocationActivity.class);
                startActivity(intent);

            }
        });

        filterByCategory = (RelativeLayout) findViewById(R.id.filter_category);
        filterByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                startActivity(intent);

            }
        });

        TextView btnCancel = (TextView)findViewById(R.id.filter_btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final LinearLayout hintSearch = (LinearLayout)findViewById(R.id.search_hint);

        final EditText txtSearch = (EditText)findViewById(R.id.search_filter);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintSearch.setVisibility(LinearLayout.GONE);
            }
        });

        TextView txtFilterByAll = (TextView) findViewById(R.id.filter_by_all);
        txtFilterByAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getBoolean(Enums.UserLoggedIn, false) == false) {
                    Intent intent8 = new Intent(FilterActivity.this, HomeActivity.class);
                    intent8.putExtra("SELECTED_INDEX_ID", "15");
                    startActivity(intent8);
                }
                else if (prefs.getBoolean(Enums.UserLoggedIn, false) == true) {
                    Intent intent8 = new Intent(FilterActivity.this, FilterscreenLC.class);
                    startActivity(intent8);
                }
            }
        });

        _wsc.loadFilterActivity(this, (ViewGroup) findViewById(R.id.filter_main));

    }

    @Override
    public void onResume()
    {
        super.onResume();
        _wsc.loadFilterActivity(this, (ViewGroup) findViewById(R.id.filter_main));
    }

}
