package com.eventus.privileb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSC;

public class LocationActivity extends AppCompatActivity {

    private WSC _wsc;
    private Context _context;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        _wsc = new WSC();
        _context = this;

        ImageView btnback = (ImageView) findViewById(R.id.location_btn_back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //final LinearLayout hintSearch = (LinearLayout)findViewById(R.id.search_hint_location);

        /* final EditText locationSearch = (EditText)findViewById(R.id.search_location);
        locationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintSearch.setVisibility(LinearLayout.GONE);
            }
        }); */

        TextView txtStartFilter = (TextView) findViewById(R.id.filter_by_location_start);
        txtStartFilter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (prefs.getBoolean(Enums.UserLoggedIn, false) == false) {
                            Intent intent8 = new Intent(LocationActivity.this, HomeActivity.class);
                            intent8.putExtra("SELECTED_INDEX_ID", "8");
                            startActivity(intent8);
                        }
                        else if (prefs.getBoolean(Enums.UserLoggedIn, false) == true){
                            Intent intent8 = new Intent(LocationActivity.this, FilterscreenL.class);
                            startActivity(intent8);
                        }
                    }
                }
        );

        _wsc.loadFilterByLocation(this, (ViewGroup) findViewById(R.id.location_main));

        final EditText editText = (EditText) findViewById(R.id.search_location);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("Inside Search", "1");
                    _wsc.getSearchInFilterByLocationResults(_context, (ViewGroup) findViewById(R.id.location_main), editText.getText().toString());
                }
                return false;
            }
        });

        ImageView ivDeleteSearchText = (ImageView) findViewById(R.id.location_cancel_search);
        ivDeleteSearchText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setText("");
                    }
                }
        );

    }
}
