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

public class CategoryActivity extends AppCompatActivity {

    private WSC _wsc;
    private Context _context;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        _wsc = new WSC();
        _context = this;
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        ImageView btnback = (ImageView) findViewById(R.id.category_btn_back);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView txtStartFilter = (TextView) findViewById(R.id.filter_by_category_start);
        txtStartFilter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (prefs.getBoolean(Enums.UserLoggedIn, false) == false) {
                            Intent intent9 = new Intent(CategoryActivity.this, HomeActivity.class);
                            intent9.putExtra("SELECTED_INDEX_ID", "9");
                            startActivity(intent9);
                        }
                        else if (prefs.getBoolean(Enums.UserLoggedIn, false) == true) {
                            Intent intent9 = new Intent(CategoryActivity.this, FilterscreenC.class);
                            startActivity(intent9);
                        }
                    }
                }
        );

        _wsc.loadFilterByCategory(this, (ViewGroup) findViewById(R.id.category_main));

        final EditText editText = (EditText) findViewById(R.id.search_category);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.i("Inside Search", "1");
                    _wsc.getSearchInFilterByCategoryResults(_context, (ViewGroup) findViewById(R.id.category_main), editText.getText().toString());
                }
                return false;
            }
        });

        ImageView ivDeleteSearchText = (ImageView) findViewById(R.id.category_cancel_search);
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
