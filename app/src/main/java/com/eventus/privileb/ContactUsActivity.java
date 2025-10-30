package com.eventus.privileb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.WSC;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by BLOOMAY on 6/19/2017.
 */

public class ContactUsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private SharedPreferences prefs;
    private TextView[] views;
    private int selectViewIndex = 0;
    private WSC _wsc;
    private GoogleMap mMap;
    private LinearLayout tabs[];
    private int selectTabIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);
        _wsc = new WSC();

        ImageView ivBtnBack = (ImageView) findViewById(R.id.contactus_btn_back);
        ivBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        if (prefs.getBoolean(Enums.UserLoggedIn, false) == false){
            final LinearLayout dropdownView = (LinearLayout) findViewById(R.id.dropdownView);
            views = new TextView[] {
                    (TextView) findViewById(R.id.card_about),
                    (TextView) findViewById(R.id.card_my),
                    (TextView) findViewById(R.id.card_benefits),
                    (TextView) findViewById(R.id.card_terms),
                    (TextView) findViewById(R.id.card_contact)
            };
            Toast.makeText(this, "Contactus", Toast.LENGTH_LONG);
            final ImageView dropButton = (ImageView) findViewById(R.id.btn_drop);
            dropButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dropdownView.getVisibility() == View.VISIBLE){
                        dropdownView.setVisibility(View.GONE);
                        dropButton.setImageResource(R.drawable.down_arrow1);
                    }else{
                        dropdownView.setVisibility(View.VISIBLE);
                        dropButton.setImageResource(R.drawable.up_arrow);


                        for (int i = 0; i < views.length; i++) {
                            views[i].setOnClickListener(viewClickListener1);
                        }
                    }
                }
            });

            tabs = new LinearLayout[] {
                    (LinearLayout)findViewById(R.id.deals_tab_privileb),
                    (LinearLayout)findViewById(R.id.deals_tab_nearby),
                    (LinearLayout)findViewById(R.id.deals_tab_card),
                    (LinearLayout)findViewById(R.id.deals_tab_categories),
                    (LinearLayout)findViewById(R.id.deals_tab_favorite)
            };

            for (int i = 0; i < tabs.length; i++) {
                tabs[i].setOnClickListener(tabClickListener1);
            }
        }
        else {
            final LinearLayout dropdownView = (LinearLayout) findViewById(R.id.dropdownView);
            views = new TextView[] {
                    (TextView) findViewById(R.id.card_about),
                    (TextView) findViewById(R.id.card_my),
                    (TextView) findViewById(R.id.card_benefits),
                    (TextView) findViewById(R.id.card_terms),
                    (TextView) findViewById(R.id.card_contact)
            };
            final ImageView dropButton = (ImageView) findViewById(R.id.btn_drop);
            dropButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dropdownView.getVisibility() == View.VISIBLE){
                        dropdownView.setVisibility(View.GONE);
                        dropButton.setImageResource(R.drawable.down_arrow1);
                    }else{
                        dropdownView.setVisibility(View.VISIBLE);
                        dropButton.setImageResource(R.drawable.up_arrow);


                        for (int i = 0; i < views.length; i++) {
                            views[i].setOnClickListener(viewClickListener2);
                        }
                    }
                }
            });
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
        }

        SupportMapFragment supportMapFragment;
        if (Build.VERSION.SDK_INT < 21) {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);
        } else {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map_contact_us);
            mapFragment.getMapAsync(this);
        }

        RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.contactus_call);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(ContactUsActivity.this).create();
                alertDialog.setTitle("Call Privileb");
                alertDialog.setMessage("81717272");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int result = ContextCompat.checkSelfPermission(ContactUsActivity.this, android.Manifest.permission.CALL_PHONE);
                                if (result == PackageManager.PERMISSION_GRANTED){
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:81717272"));
                                    startActivity(intent);
                                } else {
                                    _wsc.requestPermission(ContactUsActivity.this);
                                    int result_2 = ContextCompat.checkSelfPermission(ContactUsActivity.this, android.Manifest.permission.CALL_PHONE);
                                    if (result_2 == PackageManager.PERMISSION_GRANTED) {
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:81717272"));
                                        startActivity(intent);
                                    }
                                }
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

        RelativeLayout btnMessage = (RelativeLayout) findViewById(R.id.contactus_message);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","support@privileb.com", null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        TextView txtContactusGetDirection = (TextView) findViewById(R.id.contactus_get_directions);
        txtContactusGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    View.OnClickListener viewClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < views.length; i++) {
                if (v.getId() == views[i].getId()) { //views[i].setTextColor(0xFFD4AC5B);

                    selectViewIndex = i;
                }
                else {
                    // views[i].setTextColor(0xFF000000);
                }
            }
            showViewcontents1(selectViewIndex);
        }
    };

    View.OnClickListener viewClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < views.length; i++) {
                if (v.getId() == views[i].getId()) { //views[i].setTextColor(0xFFD4AC5B);

                    selectViewIndex = i;
                }
                else {
                    // views[i].setTextColor(0xFF000000);
                }
            }
            showViewcontents2(selectViewIndex);
        }
    };

    void showViewcontents1(int index) {

        switch (index) {

            case 0:
                Intent intent2 = new Intent(this, HomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "5");
                startActivity(intent2);
                break;
            case 1:
                Intent intent1 = new Intent(this, HomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent1);
                break;
            case 2:
                // benefits english
                Intent intent11 = new Intent(this, HomeActivity.class);
                intent11.putExtra("SELECTED_INDEX_ID", "11");
                startActivity(intent11);
                break;
            case 3:
                Intent intent13 = new Intent(this, HomeActivity.class);
                intent13.putExtra("SELECTED_INDEX_ID", "13");
                startActivity(intent13);
                break;
            case 4:

                break;
            default:
                break;
        }
    }

    void showViewcontents2(int index) {

        switch (index) {

            case 0:
                Intent intent2 = new Intent(this, LoginHomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "5");
                startActivity(intent2);
                break;
            case 1:
                Intent intent1 = new Intent(this, LoginHomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent1);
                break;
            case 2:
                // benefits english
                Intent intent11 = new Intent(this, LoginHomeActivity.class);
                intent11.putExtra("SELECTED_INDEX_ID", "11");
                startActivity(intent11);
                break;
            case 3:
                Intent intent13 = new Intent(this, LoginHomeActivity.class);
                intent13.putExtra("SELECTED_INDEX_ID", "13");
                startActivity(intent13);
                break;
            case 4:

                break;
            default:
                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        LatLng _latlng = new LatLng(Double.parseDouble("33.886181"), Double.parseDouble("35.493436"));
        mMap.addMarker(new
                MarkerOptions().position(_latlng)
                .title("Privileb")
        );

        float _zoom = 12;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(_latlng, _zoom));

    }

    View.OnClickListener tabClickListener1 = new View.OnClickListener() {
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
            showMenuContents1(selectTabIndex);
        }
    };

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

    void showMenuContents1(int index){
        switch (index) {
            case 0:
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("SELECTED_INDEX_ID", "0");
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(this, HomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "1");
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(this, HomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(this, HomeActivity.class);
                intent3.putExtra("SELECTED_INDEX_ID", "3");
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(this, HomeActivity.class);
                intent4.putExtra("SELECTED_INDEX_ID", "4");
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

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
