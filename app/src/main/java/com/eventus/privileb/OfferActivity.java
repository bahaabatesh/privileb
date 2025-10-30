package com.eventus.privileb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSCLogin;
import com.google.android.gms.maps.*;

import com.eventus.privileb.Util.WSC;

public class OfferActivity extends FragmentActivity implements OnMapReadyCallback {

    private boolean boolFavorite = false;
    private WSC _wsc;
    private WSCLogin _wsclogin;
    private GoogleMap mMap;
    LayoutInflater inflater;
    String offer_id;
    String from_view = "";
    ViewGroup current_vg;
    private SharedPreferences prefs;
    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);
        _wsc = new WSC();
        _wsclogin = new WSCLogin();

        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        if (getIntent().getStringExtra("SELECTED_OFFER_ID") != null) {
            if (getIntent().getStringExtra("SELECTED_OFFER_ID") != "") {
                offer_id = getIntent().getStringExtra("SELECTED_OFFER_ID");
                from_view = getIntent().getStringExtra("FROM_VIEW");
                ScrollView scroll_view = (ScrollView) findViewById(R.id.offer_detail_scrollview);
                current_vg = (ViewGroup) scroll_view.getParent();
            }
        }

        ImageView btnBack = (ImageView)findViewById(R.id.offer_btn_back);
        if (prefs.getBoolean(Enums.UserLoggedIn, false) == false) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        else if (prefs.getBoolean(Enums.UserLoggedIn, false) == true) {
            try {
                if (from_view.equals("featured_deals") == true) {
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else if (from_view.equals("all_deals") == true) {
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else if (from_view.equals("categories_go") == true) {
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else if (from_view.equals("filter_by_location") == true) {
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else if (from_view.equals("filter_by_categories") == true) {
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                } else if (from_view.equals("filter_by_location_and_categories") == true) {
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
                else if (from_view.equals("favorites") == true){
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
                else if (from_view.equals("nearby_offers") == true){
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
                else {
                    btnBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }
            catch (Exception ex)
            {
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }

        if (Build.VERSION.SDK_INT < 21) {
            //supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            //supportMapFragment.getMapAsync(this);
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        if (prefs.getBoolean(Enums.UserLoggedIn, false) == false) {
            _wsc.displayOfferDetails(this, current_vg, offer_id, googleMap);
        }
        else if (prefs.getBoolean(Enums.UserLoggedIn, false) == true){
            _wsclogin.checkOfferAddedToFavorite(this, current_vg, offer_id, googleMap);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if (getIntent().getStringExtra("FROM_VIEW") != null) {
                if (getIntent().getStringExtra("FROM_VIEW").equals("featured_deals") == true){
                    Intent intent = new Intent(OfferActivity.this, LoginHomeActivity.class);
                    startActivity(intent);
                }
                else if (getIntent().getStringExtra("FROM_VIEW").equals("all_deals") == true){
                    Intent intent = new Intent(OfferActivity.this, LoginHomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "6");
                    startActivity(intent);
                }
            }
            else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
