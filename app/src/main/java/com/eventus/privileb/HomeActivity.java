package com.eventus.privileb;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.LocationUpdateService;
import com.eventus.privileb.Util.WSC;
import android.location.LocationListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.navdrawer.SimpleSideDrawer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , com.google.android.gms.location.LocationListener {

    private Context context = this;
    private LinearLayout btnFilter;
    private ImageButton btnMenu;
    private ImageView viewMassage;
    private ImageView viewFood;
    private ImageView btnBack;
    private ImageView btnCardback;
    private TextView btnList;
    private TextView btnMap;
    private TextView txtTitle;
    private ImageView btnBack1;
    private LinearLayout linFilter;
    private LinearLayout nearbyFilter;
    private ImageButton nearbyMenu;
    private ImageButton nearMenu;
    private ImageView nearbyMassage;
    private ImageView nearbyFood;
    private LinearLayout linFeatured;
    private LinearLayout linAlldeals;
    private View underFeatured;
    private View underAlldeals;
    private Button btnFeatured;
    private Button btnAlldeals;
    private ImageView dropButton;
    private int selectTabIndex = 0;
    private int selectViewIndex = 0;
    private LinearLayout tabs[];
    private TextView views[];
    private RelativeLayout mainViewGroup;
    private RelativeLayout mainViewGroup1;
    private boolean bool1, bool2, bool3, bool4, bool5, bool6, bool7, bool8, bool9, bool10;
    private SharedPreferences prefs;
    private WSC _wsc;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LocationRequest mLocationRequestHighAccuracy;
    private GoogleApiClient googleApiClient;
    private Location mLastLocation;
    private String mLastUpdateTime;
    private SimpleDateFormat mDateFormat;
    private SimpleSideDrawer mNav0, mNav1, mNav2, mNav6, mNav10, mNav16;

    LayoutInflater inflater;
    LinearLayout dropdownView;
    AlertDialog progressDialogGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        _wsc = new WSC();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        tabs = new LinearLayout[] {
                (LinearLayout)findViewById(R.id.deals_tab_privileb),
                (LinearLayout)findViewById(R.id.deals_tab_nearby),
                (LinearLayout)findViewById(R.id.deals_tab_card),
                (LinearLayout)findViewById(R.id.deals_tab_categories),
                (LinearLayout)findViewById(R.id.deals_tab_favorite)
        };

        for (int i = 0; i < tabs.length; i++) {
            tabs[i].setOnClickListener(tabClickListener);
        }
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainViewGroup = (RelativeLayout)findViewById(R.id.home_main_contents);

        if (getIntent().getStringExtra("SELECTED_INDEX_ID") != null){
            if (getIntent().getStringExtra("SELECTED_INDEX_ID") != ""){
                selectTabIndex = Integer.parseInt(getIntent().getStringExtra("SELECTED_INDEX_ID"));
                if (selectTabIndex < tabs.length) {
                    tabs[selectTabIndex].setAlpha(1.0f);
                }
                for (int i = 0; i < tabs.length; i++) {
                    if (i != selectTabIndex && i != 2){
                        tabs[i].setAlpha(0.3f);
                    }
                }
            }
        }

        Log.i("Tab sel", selectTabIndex + "");
        showMaincontents(selectTabIndex);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

    }

    @Override
    protected void onStart() {
            super.onStart();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // stopService(new Intent(this, LocationUpdateService.class));
    }

    /* @Override
    public void onDestroy() {
        super.onDestroy();
        for (String path : filePaths) {
            Picasso.with(getContext()).invalidate(new File(path));
        }
    } */

    @Override
    public void onResume()
    {
        super.onResume();
    }

    View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < tabs.length; i++) {
                if (v.getId() == tabs[i].getId()) {
                    // tabs[i].setAlpha(1.0f);
                    selectTabIndex = i;
                }
                else {

                }
            }
            showMenuContents(selectTabIndex);
        }
    };

    View.OnClickListener viewClickListener = new View.OnClickListener() {
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
           Log.i("Selected", selectViewIndex +"");
            showViewcontents(selectViewIndex);
        }
    };

    void showMaincontents(int index) {

        mainViewGroup.removeAllViews();

        switch (index) {

            case 0:
                ViewGroup homeView = (ViewGroup) inflater.inflate(R.layout.activity_home_deals, null);
                mainViewGroup.addView(homeView);

                tabs[0].setAlpha(1.0f);

                if (mNav0 != null){
                    mNav0 = null;
                }
                if (mNav0 == null){
                    mNav0 = new SimpleSideDrawer(this);
                    mNav0.setLeftBehindContentView(R.layout.activity_menu);

                    RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.menu_register_btn);
                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
                    RelativeLayout btnLogin = (RelativeLayout) findViewById(R.id.menu_login_btn);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_phonecall);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                            alertDialog.setTitle("Call Charile Taxi");
                            alertDialog.setMessage("1514");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:1514"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(HomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE);
                                                if (result_2 == PackageManager.PERMISSION_GRANTED) {
                                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                                    intent.setData(Uri.parse("tel:1514"));
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

                    //add new
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:81717272"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(HomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE);
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                btnFilter = (LinearLayout) homeView.findViewById(R.id.login_btn_filter);
                btnFilter.setOnClickListener(homeClickListener);

                viewMassage = (ImageView)homeView.findViewById(R.id.deals_massage);
                viewMassage.setOnClickListener(homeClickListener);

               // viewFood = (ImageView)homeView.findViewById(R.id.deals_food);
                //viewFood.setOnClickListener(homeClickListener);

                btnMenu = (ImageButton)homeView.findViewById(R.id.deals_menu);
                btnMenu.setOnClickListener(homeClickListener);

                btnFeatured = (Button)homeView.findViewById(R.id.home_btn_featured);
                //btnFeatured.setOnClickListener(homeClickListener);

                underFeatured = (View)homeView.findViewById(R.id.underline_feature);

                btnAlldeals = (Button)homeView.findViewById(R.id.home_btn_alldeals);
                btnAlldeals.setOnClickListener(homeClickListener);

                underAlldeals = (View)homeView.findViewById(R.id.underline_alldeals);

                linFeatured = (LinearLayout)homeView.findViewById(R.id.linear_feature);
                linFeatured.setOnClickListener(homeClickListener);

                linAlldeals = (LinearLayout)homeView.findViewById(R.id.linear_alldeals);
                linAlldeals.setOnClickListener(homeClickListener);

                btnFeatured.setTextColor(0xFFD4AC5B);
                underFeatured.setVisibility(View.VISIBLE);
                btnAlldeals.setTextColor(0xFFFFFFFF);
                underAlldeals.setVisibility(View.GONE);

                WSC _wsd = new WSC();
                _wsd.displayFeaturedOffers(this, homeView);

                // startService(new Intent(this, LocationUpdateService.class));

                break;

            case 1:
                final ViewGroup nearbyView = (ViewGroup) inflater.inflate(R.layout.activity_nearby, null);
                mainViewGroup.addView(nearbyView);

                startService(new Intent(this, LocationUpdateService.class));
                tabs[1].setAlpha(1.0f);
                //final ViewGroup nearbylistView = (ViewGroup) inflater.inflate(R.layout.activity_nearby_listing, null);

                if (googleApiClient == null) {
                    googleApiClient = new GoogleApiClient.Builder(this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this).build();
                    googleApiClient.connect();

                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(30 * 1000);
                    locationRequest.setFastestInterval(5 * 1000);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);

                    //**************************
                    builder.setAlwaysShow(true); //this is the key ingredient
                    //**************************

                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            final LocationSettingsStates state = result.getLocationSettingsStates();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    progressDialogGlobal = new SpotsDialog(HomeActivity.this, R.style.Custom);
                                    progressDialogGlobal.show();
                                    SupportMapFragment supportMapFragment;
                                    if (Build.VERSION.SDK_INT < 21) {
                                        // supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_nearby);
                                        // supportMapFragment.getMapAsync(HomeActivity.this);
                                        MapFragment mapFragment = (MapFragment) getFragmentManager()
                                                .findFragmentById(R.id.map_nearby);
                                        mapFragment.getMapAsync(HomeActivity.this);
                                    } else {
                                        MapFragment mapFragment = (MapFragment) getFragmentManager()
                                                .findFragmentById(R.id.map_nearby);
                                        mapFragment.getMapAsync(HomeActivity.this);
                                    }
                                    // All location settings are satisfied. The client can initialize location
                                    // requests here.
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied. But could be fixed by showing the user
                                    // a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        status.startResolutionForResult(
                                                (Activity) HomeActivity.this , 1000);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    break;
                            }
                        }
                    });             }

                /*
                if (googleApiClient == null) {
                    googleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this).build();
                    googleApiClient.connect();

                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    locationRequest.setInterval(30 * 1000);
                    locationRequest.setFastestInterval(5 * 1000);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);

                    //**************************
                    builder.setAlwaysShow(true); //this is the key ingredient
                    //**************************

                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            final LocationSettingsStates state = result.getLocationSettingsStates();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied. The client can initialize location
                                    // requests here.
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied. But could be fixed by showing the user
                                    // a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        status.startResolutionForResult(
                                                HomeActivity.this, 1000);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied. However, we have no way to fix the
                                    // settings so we won't show the dialog.
                                    break;
                            }
                        }
                    });             } */

                RelativeLayout rlViewList = (RelativeLayout) nearbyView.findViewById(R.id.nearby_view_list);
                rlViewList.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("SELECTED_INDEX_ID", "10");
                        startActivity(intent);
                    }
                });

                TextView txtViewList = (TextView) nearbyView.findViewById(R.id.view_list);
                txtViewList.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("SELECTED_INDEX_ID", "10");
                        startActivity(intent);
                    }
                });

                nearbyFilter = (LinearLayout) nearbyView.findViewById(R.id.login_btn_filter);
                nearbyFilter.setOnClickListener(nearbyClickListener);

                if (mNav1 != null){
                    mNav1 = null;
                }
                if (mNav1 == null){
                    mNav1 = new SimpleSideDrawer(this);
                    mNav1.setLeftBehindContentView(R.layout.activity_menu);

                    RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.menu_register_btn);
                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
                    RelativeLayout btnLogin = (RelativeLayout) findViewById(R.id.menu_login_btn);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_phonecall);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
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

                    //add new
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                nearMenu = (ImageButton) nearbyView.findViewById(R.id.nearby_menu);
                nearMenu.setOnClickListener(nearbyClickListener);

                /*
                SupportMapFragment supportMapFragment;
                if (Build.VERSION.SDK_INT < 21) {
                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_nearby);
                    supportMapFragment.getMapAsync(this);
                } else {
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map_nearby);
                    mapFragment.getMapAsync(this);
                } */

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                }
                else {

                    ActivityCompat.requestPermissions((Activity) this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                break;
            case 2:

            final ViewGroup cardView = (ViewGroup) inflater.inflate(R.layout.activity_cardsignout, null);
            mainViewGroup.addView(cardView);

            tabs[2].setAlpha(1.0f);
            final ImageView btnSignoutMenu1 = (ImageView) cardView.findViewById(R.id.card_btn_back);
                btnSignoutMenu1.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        finish();
                    }
                });

                dropdownView = (LinearLayout) cardView.findViewById(R.id.dropdownView);
                views = new TextView[] {
                                (TextView) cardView.findViewById(R.id.card_about),
                                (TextView) cardView.findViewById(R.id.card_my),
                                (TextView) cardView.findViewById(R.id.card_benefits),
                                (TextView) cardView.findViewById(R.id.card_terms),
                                (TextView) cardView.findViewById(R.id.card_contact)
                        };
                dropButton = (ImageView) cardView.findViewById(R.id.btn_drop);
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
                                    views[i].setOnClickListener(viewClickListener);
                                }
                            }
                        }
                    });
            views[1].setTextColor(0xFFD4AC5B);

            Button btnBuyCard = (Button) cardView.findViewById(R.id.btn_buy_privileb_card);
            btnBuyCard.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(context, BuyCardActivity.class);
                    startActivity(intent);
                }
            });

            _wsc.loadJoinOurFamily(this, cardView);

            break;
            case 3:
                ViewGroup categoriesView = (ViewGroup) inflater.inflate(R.layout.activity_categories, null);
                mainViewGroup.addView(categoriesView);

                tabs[3].setAlpha(1.0f);

                btnBack = (ImageView) categoriesView.findViewById(R.id.categories_btn_back);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                TextView txtCategoriesGo = (TextView) categoriesView.findViewById(R.id.categories_go);
                txtCategoriesGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent7 = new Intent(context, HomeActivity.class);
                        intent7.putExtra("SELECTED_INDEX_ID", "7");
                        startActivity(intent7);
                    }});

                _wsc.loadCategories(this, categoriesView);

                break;
            case 4:

                ViewGroup favoriteView =(ViewGroup) inflater.inflate(R.layout.activity_favorite, null);
                mainViewGroup.addView(favoriteView);

                tabs[4].setAlpha(1.0f);

                btnBack1 = (ImageView) favoriteView.findViewById(R.id.favorite_btn_back);
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

                final Button favorites_login = (Button) findViewById(R.id.favorites_login);
                favorites_login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent4 = new Intent(context, LoginActivity.class);
                        startActivity(intent4);
                    }
                });

                break;
            case 5:
                final ViewGroup cardViewCard = (ViewGroup) inflater.inflate(R.layout.activity_card, null);
                mainViewGroup.addView(cardViewCard);

                dropdownView = (LinearLayout) cardViewCard.findViewById(R.id.dropdownView);
                views = new TextView[] {
                        (TextView) cardViewCard.findViewById(R.id.card_about),
                        (TextView) cardViewCard.findViewById(R.id.card_my),
                        (TextView) cardViewCard.findViewById(R.id.card_benefits),
                        (TextView) cardViewCard.findViewById(R.id.card_terms),
                        (TextView) cardViewCard.findViewById(R.id.card_contact)
                };

                dropButton = (ImageView) cardViewCard.findViewById(R.id.btn_drop);
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
                                views[i].setOnClickListener(viewClickListener);
                            }
                        }
                    }
                });
                views[0].setTextColor(0xFFD4AC5B);

                btnCardback = (ImageView) cardViewCard.findViewById(R.id.card_btn_back);
                btnCardback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       finish();
                    }
                });
                mainViewGroup1 = (RelativeLayout) cardViewCard.findViewById(R.id.card_main_contents);
                txtTitle = (TextView) cardViewCard.findViewById(R.id.mrs_title);

                ViewGroup cardViewCardDown = (ViewGroup) inflater.inflate(R.layout.activity_carddown, null);
                mainViewGroup1.addView(cardViewCardDown);
                txtTitle.setText("ABOUT PRIVILEB CARD");
                dropButton.setImageResource(R.drawable.down_arrow1);
                dropdownView.setVisibility(View.GONE);

                _wsc.loadAboutUsEng(this, cardViewCardDown);

                break;
            case 6:
                ViewGroup homeView6 = (ViewGroup) inflater.inflate(R.layout.activity_home_deals, null);
                mainViewGroup.addView(homeView6);

                if (mNav6 != null){
                    mNav6 = null;
                }
                if (mNav6 == null){
                    mNav6 = new SimpleSideDrawer(this);
                    mNav6.setLeftBehindContentView(R.layout.activity_menu);

                    RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.menu_register_btn);
                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
                    RelativeLayout btnLogin = (RelativeLayout) findViewById(R.id.menu_login_btn);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_phonecall);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
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

                    //add new
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                }
                btnFilter = (LinearLayout) homeView6.findViewById(R.id.login_btn_filter);
                btnFilter.setOnClickListener(homeClickListener);

                viewMassage = (ImageView)homeView6.findViewById(R.id.deals_massage);
                viewMassage.setOnClickListener(homeClickListener);

                // viewFood = (ImageView)homeView.findViewById(R.id.deals_food);
                //viewFood.setOnClickListener(homeClickListener);

                btnMenu = (ImageButton)homeView6.findViewById(R.id.deals_menu);
                btnMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // contact us
                        mNav6.toggleLeftDrawer();
                    }
                });

                btnFeatured = (Button)homeView6.findViewById(R.id.home_btn_featured);
                btnFeatured.setOnClickListener(homeClickListener);

                underFeatured = (View)homeView6.findViewById(R.id.underline_feature);

                btnAlldeals = (Button)homeView6.findViewById(R.id.home_btn_alldeals);
                // btnAlldeals.setOnClickListener(homeClickListener);

                underAlldeals = (View)homeView6.findViewById(R.id.underline_alldeals);

                linFeatured = (LinearLayout)homeView6.findViewById(R.id.linear_feature);
                linFeatured.setOnClickListener(homeClickListener);

                linAlldeals = (LinearLayout)homeView6.findViewById(R.id.linear_alldeals);
                linAlldeals.setOnClickListener(homeClickListener);

                btnAlldeals.setTextColor(0xFFD4AC5B);
                underAlldeals.setVisibility(View.VISIBLE);
                btnFeatured.setTextColor(0xFFFFFFFF);
                underFeatured.setVisibility(View.GONE);

                tabs[0].setAlpha(1.0f);

                WSC _wsc = new WSC();
                _wsc.displayAllDeals(this, homeView6);

                break;
            case 7:
                // categories go
                ViewGroup categoriesGoView = (ViewGroup) inflater.inflate(R.layout.activity_filterscreen, null);
                mainViewGroup.addView(categoriesGoView);

                btnFilter = (LinearLayout) categoriesGoView.findViewById(R.id.login_btn_filter);
                btnFilter.setOnClickListener(homeClickListener);

                btnMenu = (ImageButton)categoriesGoView.findViewById(R.id.deals_menu);
                btnMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                underFeatured = (View)categoriesGoView.findViewById(R.id.underline_feature);
                underAlldeals = (View)categoriesGoView.findViewById(R.id.underline_alldeals);

                final WSC _wsc7 = new WSC();

                final EditText editText_categoriesgo = (EditText) categoriesGoView.findViewById(R.id.filterscreen_search);
                editText_categoriesgo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsc7.filterOffersInFilterScreenCategoriesGo(HomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), editText_categoriesgo.getText().toString());
                        }
                        return false;
                    }
                });

                _wsc7.displayCategoriesGo(this, categoriesGoView);

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                break;
            case 8:
                // filter by location view
                ViewGroup filterByLocationView = (ViewGroup) inflater.inflate(R.layout.activity_filterscreen, null);
                mainViewGroup.addView(filterByLocationView);

                btnFilter = (LinearLayout) filterByLocationView.findViewById(R.id.login_btn_filter);
                btnFilter.setOnClickListener(homeClickListener);

                btnMenu = (ImageButton)filterByLocationView.findViewById(R.id.deals_menu);
                btnMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                underFeatured = (View)filterByLocationView.findViewById(R.id.underline_feature);
                underAlldeals = (View)filterByLocationView.findViewById(R.id.underline_alldeals);

                final WSC _wsc8 = new WSC();
                _wsc8.displayFilterByLocationView(this, filterByLocationView);

                final EditText editText = (EditText) filterByLocationView.findViewById(R.id.filterscreen_search);
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsc8.filterOffersInFilterScreenLocation(HomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), editText.getText().toString());
                        }
                        return false;
                    }
                });

                ImageView ivDeleteSearchText = (ImageView) filterByLocationView.findViewById(R.id.filterscreen_cancel_search);
                ivDeleteSearchText.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText.setText("");
                            }
                        }
                );

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                break;
            case 9:
                // filter by categories view
                ViewGroup filterByCategoriesView = (ViewGroup) inflater.inflate(R.layout.activity_filterscreen, null);
                mainViewGroup.addView(filterByCategoriesView);

                btnFilter = (LinearLayout) filterByCategoriesView.findViewById(R.id.login_btn_filter);
                btnFilter.setOnClickListener(homeClickListener);

                btnMenu = (ImageButton)filterByCategoriesView.findViewById(R.id.deals_menu);
                btnMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                underFeatured = (View)filterByCategoriesView.findViewById(R.id.underline_feature);
                underAlldeals = (View)filterByCategoriesView.findViewById(R.id.underline_alldeals);

                final WSC _wsc9 = new WSC();
                _wsc9.displayFilterByCategoriesView(this, filterByCategoriesView);

                final EditText editText_categories = (EditText) filterByCategoriesView.findViewById(R.id.filterscreen_search);
                editText_categories.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsc9.filterOffersInFilterScreenCategories(HomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), editText_categories.getText().toString());
                        }
                        return false;
                    }
                });

                ImageView ivDeleteSearchText_categories = (ImageView) filterByCategoriesView.findViewById(R.id.filterscreen_cancel_search);
                ivDeleteSearchText_categories.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText_categories.setText("");
                            }
                        }
                );

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                break;
            case 10:
                final ViewGroup nearbylistView = (ViewGroup) inflater.inflate(R.layout.activity_nearby_listing, null);
                mainViewGroup.addView(nearbylistView);

                startService(new Intent(this, LocationUpdateService.class));
                btnFilter = (LinearLayout) nearbylistView.findViewById(R.id.nearby_listing_filter);
                btnFilter.setOnClickListener(homeClickListener);

                RelativeLayout rlViewMap = (RelativeLayout) nearbylistView.findViewById(R.id.nearby_view_map);
                rlViewMap.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("SELECTED_INDEX_ID", "1");
                        startActivity(intent);
                    }
                });

                TextView txtViewMap = (TextView) nearbylistView.findViewById(R.id.view_map);
                txtViewMap.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("SELECTED_INDEX_ID", "1");
                        startActivity(intent);
                    }
                });

                if (mNav10 != null){
                    mNav10 = null;
                }
                if (mNav10 == null){
                    mNav10 = new SimpleSideDrawer(this);
                    mNav10.setLeftBehindContentView(R.layout.activity_menu);

                    RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.menu_register_btn);
                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
                    RelativeLayout btnLogin = (RelativeLayout) findViewById(R.id.menu_login_btn);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_phonecall);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
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

                    //add new
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                nearMenu = (ImageButton) nearbylistView.findViewById(R.id.nearby_listing_menu);
                nearMenu.setOnClickListener(nearbyClickListener);

                progressDialogGlobal = new SpotsDialog(HomeActivity.this, R.style.Custom);
                progressDialogGlobal.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Log.i("avocadol", prefs.getString(Enums.CurrentLatitude, "") + " " + prefs.getString(Enums.CurrentLongitude, ""));
                        WSC _wsc10 = new WSC();
                        stopService(new Intent(HomeActivity.this, LocationUpdateService.class));
                        _wsc10.displayNearbyOffersListing(HomeActivity.this, prefs.getString(Enums.CurrentLatitude, ""), prefs.getString(Enums.CurrentLongitude, ""), nearbylistView);
                        progressDialogGlobal.dismiss();
                    }
                }, 3000);

                break;
            case 11:
                // benefits english
                ViewGroup benefitsView = (ViewGroup) inflater.inflate(R.layout.activity_benefits, null);
                mainViewGroup.addView(benefitsView);

                mainViewGroup1 = (RelativeLayout) benefitsView.findViewById(R.id.cardsignout_main_contents);
                ImageView ivBtnBack = (ImageView) benefitsView.findViewById(R.id.benefits_btn_back);
                ivBtnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       finish();
                    }
                });

                dropdownView = (LinearLayout) benefitsView.findViewById(R.id.dropdownView);
                views = new TextView[] {
                        (TextView) benefitsView.findViewById(R.id.card_about),
                        (TextView) benefitsView.findViewById(R.id.card_my),
                        (TextView) benefitsView.findViewById(R.id.card_benefits),
                        (TextView) benefitsView.findViewById(R.id.card_terms),
                        (TextView) benefitsView.findViewById(R.id.card_contact)
                };
                dropButton = (ImageView) benefitsView.findViewById(R.id.btn_drop);
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
                                views[i].setOnClickListener(viewClickListener);
                            }
                        }
                    }
                });

                views[2].setTextColor(0xFFD4AC5B);
                Button btnBenefitsEnglish = (Button) benefitsView.findViewById(R.id.benefits_english);
                Button btnBenefitsArabic = (Button) benefitsView.findViewById(R.id.benefits_arabic);
                final View vBenefitsEnglishUnderline = (View) benefitsView.findViewById(R.id.benefits_english_underline);
                final View vBenefitsArabicUnderline = (View) benefitsView.findViewById(R.id.benefits_arabic_underline);

                vBenefitsEnglishUnderline.setVisibility(LinearLayout.VISIBLE);
                btnBenefitsEnglish.setTextColor(getResources().getColor(R.color.colorDarkBeige));
                vBenefitsArabicUnderline.setVisibility(LinearLayout.GONE);
                btnBenefitsArabic.setTextColor(getResources().getColor(R.color.colorWhite));

                btnBenefitsEnglish.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                          }
                                                      }
                );

                btnBenefitsArabic.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             Intent intent2 = new Intent(context, HomeActivity.class);
                                                             intent2.putExtra("SELECTED_INDEX_ID", "12");
                                                             startActivity(intent2);
                                                         }
                                                     }
                );

                WSC _wsc11 = new WSC();
                _wsc11.loadBenefitsEng(this, benefitsView);

                break;
            case 12:
                // benefits arabic
                ViewGroup benefitsViewAr = (ViewGroup) inflater.inflate(R.layout.activity_benefits, null);
                mainViewGroup.addView(benefitsViewAr);

                mainViewGroup1 = (RelativeLayout) benefitsViewAr.findViewById(R.id.cardsignout_main_contents);
                ImageView ivBtnBackAr = (ImageView) benefitsViewAr.findViewById(R.id.benefits_btn_back);
                ivBtnBackAr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                dropdownView = (LinearLayout) benefitsViewAr.findViewById(R.id.dropdownView);
                views = new TextView[] {
                        (TextView) benefitsViewAr.findViewById(R.id.card_about),
                        (TextView) benefitsViewAr.findViewById(R.id.card_my),
                        (TextView) benefitsViewAr.findViewById(R.id.card_benefits),
                        (TextView) benefitsViewAr.findViewById(R.id.card_terms),
                        (TextView) benefitsViewAr.findViewById(R.id.card_contact)
                };
                dropButton = (ImageView) benefitsViewAr.findViewById(R.id.btn_drop);
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
                                views[i].setOnClickListener(viewClickListener);
                            }
                        }
                    }
                });
                views[2].setTextColor(0xFFD4AC5B);

                Button _btnBenefitsEnglish = (Button) benefitsViewAr.findViewById(R.id.benefits_english);
                Button _btnBenefitsArabic = (Button) benefitsViewAr.findViewById(R.id.benefits_arabic);
                final View _vBenefitsEnglishUnderline = (View) benefitsViewAr.findViewById(R.id.benefits_english_underline);
                final View _vBenefitsArabicUnderline = (View) benefitsViewAr.findViewById(R.id.benefits_arabic_underline);

                _vBenefitsEnglishUnderline.setVisibility(LinearLayout.GONE);
                _btnBenefitsEnglish.setTextColor(getResources().getColor(R.color.colorWhite));
                _vBenefitsArabicUnderline.setVisibility(LinearLayout.VISIBLE);
                _btnBenefitsArabic.setTextColor(getResources().getColor(R.color.colorDarkBeige));

                _btnBenefitsEnglish.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Intent intent2 = new Intent(context, HomeActivity.class);
                                                              intent2.putExtra("SELECTED_INDEX_ID", "11");
                                                              startActivity(intent2);
                                                          }
                                                      }
                );

                _btnBenefitsArabic.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {

                                                         }
                                                     }
                );

                WSC _wsc12 = new WSC();
                _wsc12.loadBenefitsAr(this, benefitsViewAr);

                break;
            case 13:
                // terms and conditions english
                    ViewGroup termsView = (ViewGroup) inflater.inflate(R.layout.activity_terms, null);
                    mainViewGroup.addView(termsView);

                    ImageView ivBtnBackTerms = (ImageView) termsView.findViewById(R.id.benefits_btn_back);
                    ivBtnBackTerms.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

                    dropdownView = (LinearLayout) termsView.findViewById(R.id.dropdownView);
                    views = new TextView[] {
                            (TextView) termsView.findViewById(R.id.card_about),
                            (TextView) termsView.findViewById(R.id.card_my),
                            (TextView) termsView.findViewById(R.id.card_benefits),
                            (TextView) termsView.findViewById(R.id.card_terms),
                            (TextView) termsView.findViewById(R.id.card_contact)
                    };
                    dropButton = (ImageView) termsView.findViewById(R.id.btn_drop);
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
                                    views[i].setOnClickListener(viewClickListener);
                                }
                            }
                        }
                    });
                    views[3].setTextColor(0xFFD4AC5B);

                    Button btnTermsEnglish = (Button) termsView.findViewById(R.id.terms_english);
                    Button btnTermsArabic = (Button) termsView.findViewById(R.id.terms_arabic);
                    final View vTermsEnglishUnderline = (View) termsView.findViewById(R.id.terms_english_underline);
                    final View vTermsArabicUnderline = (View) termsView.findViewById(R.id.terms_arabic_underline);

                    vTermsEnglishUnderline.setVisibility(LinearLayout.VISIBLE);
                    btnTermsEnglish.setTextColor(getResources().getColor(R.color.colorDarkBeige));
                    vTermsArabicUnderline.setVisibility(LinearLayout.GONE);
                    btnTermsArabic.setTextColor(getResources().getColor(R.color.colorWhite));

                    btnTermsEnglish.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                           }
                                                       }
                    );

                    btnTermsArabic.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              Intent intent2 = new Intent(context, HomeActivity.class);
                                                              intent2.putExtra("SELECTED_INDEX_ID", "14");
                                                              startActivity(intent2);
                                                          }
                                                      }
                    );

                WSC _wsc13 = new WSC();
                _wsc13.loadTermsAndConditionsEng(this, termsView);

            break;
            case 14:
                // terms and conditions arabic
                ViewGroup termsArabicView = (ViewGroup) inflater.inflate(R.layout.activity_terms, null);
                mainViewGroup.addView(termsArabicView);

                ImageView ivBackTermsAr = (ImageView) termsArabicView.findViewById(R.id.benefits_btn_back);
                ivBackTermsAr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                dropdownView = (LinearLayout) termsArabicView.findViewById(R.id.dropdownView);
                views = new TextView[] {
                        (TextView) termsArabicView.findViewById(R.id.card_about),
                        (TextView) termsArabicView.findViewById(R.id.card_my),
                        (TextView) termsArabicView.findViewById(R.id.card_benefits),
                        (TextView) termsArabicView.findViewById(R.id.card_terms),
                        (TextView) termsArabicView.findViewById(R.id.card_contact)
                };
                dropButton = (ImageView) termsArabicView.findViewById(R.id.btn_drop);
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
                                views[i].setOnClickListener(viewClickListener);
                            }
                        }
                    }
                });
                views[3].setTextColor(0xFFD4AC5B);

                Button btn_TermsEnglish = (Button) termsArabicView.findViewById(R.id.terms_english);
                Button btn_TermsArabic = (Button) termsArabicView.findViewById(R.id.terms_arabic);
                final View v_TermsEnglishUnderline = (View) termsArabicView.findViewById(R.id.terms_english_underline);
                final View v_TermsArabicUnderline = (View) termsArabicView.findViewById(R.id.terms_arabic_underline);

                v_TermsEnglishUnderline.setVisibility(LinearLayout.GONE);
                btn_TermsEnglish.setTextColor(getResources().getColor(R.color.colorWhite));
                v_TermsArabicUnderline.setVisibility(LinearLayout.VISIBLE);
                btn_TermsArabic.setTextColor(getResources().getColor(R.color.colorDarkBeige));

                btn_TermsEnglish.setOnClickListener(new View.OnClickListener() {
                                                       @Override
                                                       public void onClick(View v) {
                                                           Intent intent2 = new Intent(context, HomeActivity.class);
                                                           intent2.putExtra("SELECTED_INDEX_ID", "13");
                                                           startActivity(intent2);
                                                       }
                                                   }
                );

                btn_TermsArabic.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {

                                                      }
                                                  }
                );

                WSC _wsc14 = new WSC();
                _wsc14.loadTermsAndConditionsAr(this, termsArabicView);

                break;
            case 15:
                // filter by both locatiosn and categories view
                ViewGroup filterByLocationAndCategoriesView = (ViewGroup) inflater.inflate(R.layout.activity_filterscreen, null);
                mainViewGroup.addView(filterByLocationAndCategoriesView);

                btnFilter = (LinearLayout) filterByLocationAndCategoriesView.findViewById(R.id.login_btn_filter);
                btnFilter.setOnClickListener(homeClickListener);

                btnMenu = (ImageButton)filterByLocationAndCategoriesView.findViewById(R.id.deals_menu);
                btnMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                underFeatured = (View)filterByLocationAndCategoriesView.findViewById(R.id.underline_feature);
                underAlldeals = (View)filterByLocationAndCategoriesView.findViewById(R.id.underline_alldeals);

                final WSC _wsc15 = new WSC();
                _wsc15.displayFilterByLocationAndCategoriesView(this, filterByLocationAndCategoriesView);

                final EditText editText_both = (EditText) filterByLocationAndCategoriesView.findViewById(R.id.filterscreen_search);
                editText_both.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsc15.filterOffersInFilterScreenLocationAndCategories(HomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), editText_both.getText().toString());
                        }
                        return false;
                    }
                });

                ImageView ivDeleteSearchText_both = (ImageView) filterByLocationAndCategoriesView.findViewById(R.id.filterscreen_cancel_search);
                ivDeleteSearchText_both.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editText_both.setText("");
                            }
                        }
                );

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                break;
            case 16:
                final ViewGroup cardView1 = (ViewGroup) inflater.inflate(R.layout.activity_cardsignout, null);
                mainViewGroup.addView(cardView1);

                // mainViewGroup1 = (RelativeLayout) cardView.findViewById(R.id.cardsignout_main_contents);

                dropdownView = (LinearLayout) cardView1.findViewById(R.id.dropdownView);
                views = new TextView[] {
                        (TextView) cardView1.findViewById(R.id.card_about),
                        (TextView) cardView1.findViewById(R.id.card_my),
                        (TextView) cardView1.findViewById(R.id.card_benefits),
                        (TextView) cardView1.findViewById(R.id.card_terms),
                        (TextView) cardView1.findViewById(R.id.card_contact)
                };
                dropButton = (ImageView) cardView1.findViewById(R.id.btn_drop);
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
                                views[i].setOnClickListener(viewClickListener);
                            }
                        }
                    }
                });
                views[1].setTextColor(0xFFD4AC5B);

                Button btnBuyCard1 = (Button) cardView1.findViewById(R.id.btn_buy_privileb_card);
                btnBuyCard1.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, BuyCardActivity.class);
                        startActivity(intent);
                    }
                });

                /*final ImageView btnSignoutMenu1 = (ImageView) cardView1.findViewById(R.id.cardsignout_menu);
                btnSignoutMenu1.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                       mNav16.toggleLeftDrawer();
                    }
                }); */

                if (mNav16 != null){
                    mNav16 = null;
                }
                if (mNav16 == null){
                    mNav16 = new SimpleSideDrawer(this);
                    mNav16.setLeftBehindContentView(R.layout.activity_menu);

                    RelativeLayout btnRegister = (RelativeLayout) findViewById(R.id.menu_register_btn);
                    btnRegister.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RegisterActivity.class);
                            startActivity(intent);
                        }
                    });
                    RelativeLayout btnLogin = (RelativeLayout) findViewById(R.id.menu_login_btn);
                    btnLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_phonecall);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
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

                    //add new
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(HomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                txtTitle = (TextView) cardView1.findViewById(R.id.mrs_title);

                break;
            default:
                break;
        }
    }

    void showMenuContents(int index){
        switch (index) {
            case 0:
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("SELECTED_INDEX_ID", "0");
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(context, HomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "1");
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(context, HomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(context, HomeActivity.class);
                intent3.putExtra("SELECTED_INDEX_ID", "3");
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(context, HomeActivity.class);
                intent4.putExtra("SELECTED_INDEX_ID", "4");
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

    void showViewcontents(int index) {

        // mainViewGroup1.removeAllViews();

        switch (index) {

            case 0:
                Intent intent2 = new Intent(context, HomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "5");
                startActivity(intent2);
                break;
            case 1:
                Log.i("Inside mycard", "true");
                Intent intent1 = new Intent(context, HomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent1);
                break;
            case 2:
                // benefits english
                Intent intent11 = new Intent(context, HomeActivity.class);
                intent11.putExtra("SELECTED_INDEX_ID", "11");
                startActivity(intent11);
            break;
            case 3:
                Intent intent13 = new Intent(context, HomeActivity.class);
                intent13.putExtra("SELECTED_INDEX_ID", "13");
                startActivity(intent13);
                break;
            case 4:
                Intent intent = new Intent(this, ContactUsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    View.OnClickListener homeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.login_btn_filter:
                    Intent intent = new Intent(context, FilterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.nearby_listing_filter:
                    Intent intentListing = new Intent(context, FilterActivity.class);
                    startActivity(intentListing);
                    break;
                case R.id.deals_massage:
                    Intent intent1 = new Intent(context, OfferActivity.class);
                    startActivity(intent1);
                    break;
                /*case R.id.deals_food:
                    Intent intent2 = new Intent(context, OfferActivity.class);
                    startActivity(intent2);
                    break; */
                case R.id.deals_menu:
                    mNav0.toggleLeftDrawer();
                    //btnMenu.setImageResource(R.drawable.color_menu);
                    //Intent intent3 = new Intent(context, MenuActivity.class);
                    //startActivity(intent3);
                    break;
                case R.id.home_btn_featured:
                    Intent intent0 = new Intent(context, HomeActivity.class);
                    intent0.putExtra("SELECTED_INDEX_ID", "0");
                    startActivity(intent0);
                    break;
                case R.id.home_btn_alldeals:
                    Intent intent6 = new Intent(context, HomeActivity.class);
                    intent6.putExtra("SELECTED_INDEX_ID", "6");
                    startActivity(intent6);
                    break;
                default:
                    break;
            }
        }
    };

    View.OnClickListener nearbyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.nearby_listing_menu:
                    mNav10.toggleLeftDrawer();
                    break;
                case R.id.nearby_menu:
                    mNav1.toggleLeftDrawer();
                    break;
                case R.id.login_btn_filter:
                    Intent intent3 = new Intent(context, FilterActivity.class);
                    startActivity(intent3);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        double latitude;
        double longitude;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("avocado", prefs.getString(Enums.CurrentLatitude, "") + " " + prefs.getString(Enums.CurrentLongitude, ""));
                stopService(new Intent(HomeActivity.this, LocationUpdateService.class));
                _wsc.displayNearbyOffersMap(context, prefs.getString(Enums.CurrentLatitude, ""), prefs.getString(Enums.CurrentLongitude, ""), mMap);
                progressDialogGlobal.dismiss();
            }
        }, 3000);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Connection failed");
        if (!googleApiClient.isConnecting() &&
                !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLastUpdateTime = getTimestamp();
        if (mLastLocation == null) {
            Log.e("LocationUpdateService", "Cannot retrieve location");
        }

        /**
         * Write activity confidences to database
         */
        // add code here
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Log.i("Location updated", Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
        boolean isActive = myPref.getBoolean("pref_key_services", true);
        /**
         * stop location updates if settings set to false
         */
        if (!isActive) {
            stopLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }

    /**
     * @return timestamp string with format yyyy-MM-dd HH:mm:ss.SSSZ
     */
    private String getTimestamp() {
        if (mDateFormat == null) {
            // Get a date formatter, and catch errors in the returned timestamp
            try {
                mDateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            } catch (Exception e) {
                return null;
            }
            // Format the timestamp according to the pattern, then localize the pattern
            mDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss.SSSZ");
            mDateFormat.applyLocalizedPattern(mDateFormat.toLocalizedPattern());
        }
        return mDateFormat.format(new Date());
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connected Suspended");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                progressDialogGlobal = new SpotsDialog(HomeActivity.this, R.style.Custom);
                progressDialogGlobal.show();
                SupportMapFragment supportMapFragment;
                if (Build.VERSION.SDK_INT < 21) {
                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_nearby);
                    supportMapFragment.getMapAsync(HomeActivity.this);
                } else {
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map_nearby);
                    mapFragment.getMapAsync(HomeActivity.this);
                }
            }
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(".Util.LocationUpdateService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}