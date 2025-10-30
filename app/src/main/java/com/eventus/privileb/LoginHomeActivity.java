package com.eventus.privileb;

import android.Manifest;
import android.app.Activity;
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

import com.eventus.privileb.Util.WSCLogin;
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

public class LoginHomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
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
    private WSCLogin _wsclogingeneral;
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
        _wsclogingeneral = new WSCLogin();

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

        showMaincontents(selectTabIndex);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

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
                    /* if (i != 2) {
                        tabs[i].setAlpha(0.3f);
                    } */
                }
            }
            showMenuContents(selectTabIndex);
        }
    };

    View.OnClickListener viewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < views.length; i++) {
                if (v.getId() == views[i].getId()) { // views[i].setTextColor(0xFFD4AC5B);

                    selectViewIndex = i;
                }
                else {
                    // views[i].setTextColor(0xFF000000);
                }
            }
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
                    mNav0.setLeftBehindContentView(R.layout.activity_menu_login);

                    TextView txtUsername = (TextView) findViewById(R.id.login_menu_user_name);
                    txtUsername.setText(prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));

                    TextView txtLogout = (TextView) findViewById(R.id.btn_signout);
                    txtLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // implement logout funcionality
                            _wsclogingeneral.logout(LoginHomeActivity.this);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_login_phone);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Charile Taxi");
                            alertDialog.setMessage("1514");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:1514"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.login_menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:81717272"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_login_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_login_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FeaturedDealsActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_login_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_login_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_login_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llFavorites = (LinearLayout) findViewById(R.id.menu_login_favorites);
                    llFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FavoriteActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_login_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_login_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(LoginHomeActivity.this, ContactUsActivity.class);
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
                btnMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // contact us
                       mNav0.toggleLeftDrawer();
                    }
                });

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

                WSCLogin _wsd = new WSCLogin();
                _wsd.getUserFavoritesFeaturedDeals(this, homeView, prefs.getLong(Enums.UserId, 0) + "", "featured_deals", "");


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
                                    progressDialogGlobal = new SpotsDialog(LoginHomeActivity.this, R.style.Custom);
                                    progressDialogGlobal.show();
                                    SupportMapFragment supportMapFragment;
                                    if (Build.VERSION.SDK_INT < 21) {
                                        // supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_nearby);
                                        // supportMapFragment.getMapAsync(LoginHomeActivity.this);
                                        MapFragment mapFragment = (MapFragment) getFragmentManager()
                                                .findFragmentById(R.id.map_nearby);
                                        mapFragment.getMapAsync(LoginHomeActivity.this);
                                    } else {
                                        MapFragment mapFragment = (MapFragment) getFragmentManager()
                                                .findFragmentById(R.id.map_nearby);
                                        mapFragment.getMapAsync(LoginHomeActivity.this);
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
                                                (Activity) LoginHomeActivity.this , 1000);
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
                        Intent intent = new Intent(context, NearbyListingActivity.class);
                        startActivity(intent);
                    }
                });

                TextView txtViewList = (TextView) nearbyView.findViewById(R.id.view_list);
                txtViewList.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, NearbyListingActivity.class);
                        startActivity(intent);
                    }
                });

                /*btnMap = (TextView) nearbyView.findViewById(R.id.view_map);
                btnMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, LoginHomeActivity.class);
                        intent.putExtra("SELECTED_INDEX_ID", "1");
                        startActivity(intent);
                    }
                }); */

                //linFilter = (LinearLayout) nearbyView.findViewById(R.id.login_lay_filter);
                //linFilter.setOnClickListener(nearbyClickListener);


                /* nearbyMenu = (ImageButton) nearbyView.findViewById(R.id.nearby_listing_menu);
                nearbyMenu.setOnClickListener(nearbyClickListener);

                nearbyMassage = (ImageView) nearbyView.findViewById(R.id.nearby_massage);
                nearbyMassage.setOnClickListener(nearbyClickListener);

                nearbyFood = (ImageView) nearbyView.findViewById(R.id.nearby_food);
                nearbyFood.setOnClickListener(nearbyClickListener); */

                nearbyFilter = (LinearLayout) nearbyView.findViewById(R.id.login_btn_filter);
                nearbyFilter.setOnClickListener(nearbyClickListener);

                if (mNav1 != null){
                    mNav1 = null;
                }
                if (mNav1 == null){
                    mNav1 = new SimpleSideDrawer(this);
                    mNav1.setLeftBehindContentView(R.layout.activity_menu_login);

                    TextView txtUsername = (TextView) findViewById(R.id.login_menu_user_name);
                    txtUsername.setText(prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));

                    TextView txtLogout = (TextView) findViewById(R.id.btn_signout);
                    txtLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // implement logout funcionality
                            _wsclogingeneral.logout(LoginHomeActivity.this);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_login_phone);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Charile Taxi");
                            alertDialog.setMessage("1514");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:1514"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.login_menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:81717272"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_login_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_login_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FeaturedDealsActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_login_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_login_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_login_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llFavorites = (LinearLayout) findViewById(R.id.menu_login_favorites);
                    llFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FavoriteActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_login_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_login_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(LoginHomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                nearMenu = (ImageButton) nearbyView.findViewById(R.id.nearby_menu);
                nearMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // contact us
                        mNav1.toggleLeftDrawer();
                    }
                });

                /*
                SupportMapFragment supportMapFragment;
                if (Build.VERSION.SDK_INT < 21) {
                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_nearby);
                    supportMapFragment.getMapAsync(this);
                } else {
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map_nearby);
                    mapFragment.getMapAsync(this);
                }
                */

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.ACCESS_FINE_LOCATION))
                {
                }
                else {

                    ActivityCompat.requestPermissions((Activity) this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                break;
            case 2:

                final ViewGroup cardView = (ViewGroup) inflater.inflate(R.layout.activity_card, null);
                mainViewGroup.addView(cardView);

                tabs[2].setAlpha(1.0f);
                // mainViewGroup1 = (RelativeLayout) cardView.findViewById(R.id.cardsignout_main_contents);

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

                _wsclogingeneral.loadCard(this, cardView, prefs.getLong(Enums.UserId, 0) + "");

                /* final ImageView btnSignoutMenu = (ImageView) cardView.findViewById(R.id.cardsignout_menu);
                btnSignoutMenu.setOnClickListener(homeClickListener);

                if (PrivilebApplication.mNav != null){
                    PrivilebApplication.mNav = null;
                }
                if (PrivilebApplication.mNav == null){
                    PrivilebApplication.mNav = new SimpleSideDrawer(this);
                    PrivilebApplication.mNav.setLeftBehindContentView(R.layout.activity_menu_login);

                    TextView txtUsername = (TextView) findViewById(R.id.login_menu_user_name);
                    txtUsername.setText(prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));

                    TextView txtLogout = (TextView) findViewById(R.id.btn_signout);
                    txtLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // implement logout funcionality
                            _wsclogingeneral.logout(LoginHomeActivity.this);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_login_phone);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Charile Taxi");
                            alertDialog.setMessage("1514");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:1514"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.login_menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:81717272"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_login_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_login_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FeaturedDealsActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_login_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_login_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_login_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llFavorites = (LinearLayout) findViewById(R.id.menu_login_favorites);
                    llFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FavoriteActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_login_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(LoginHomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });
                } */
                // mainViewGroup1 = (RelativeLayout) cardView.findViewById(R.id.card_main_contents);

                txtTitle = (TextView) cardView.findViewById(R.id.mrs_title);
                if (prefs.getString(Enums.UserGender, "").equals("male") == true){
                    txtTitle.setText("Mr. " + prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));
                }
                else if (prefs.getString(Enums.UserGender, "").equals("female") == true){
                    txtTitle.setText("Mrs. " + prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));
                }


            btnCardback = (ImageView) cardView.findViewById(R.id.card_btn_back);
            btnCardback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

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
                        Intent intent7 = new Intent(context, FilterscreenCG.class);
                        startActivity(intent7);
                    }});

                _wsc.loadCategories(this, categoriesView);

                break;
            case 4:

                ViewGroup favoriteView =(ViewGroup) inflater.inflate(R.layout.activity_favorite, null);
                mainViewGroup.addView(favoriteView);

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

                _wsclogingeneral.loadFavorites(LoginHomeActivity.this, favoriteView, prefs.getLong(Enums.UserId, 0) + "");

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
                    mNav6.setLeftBehindContentView(R.layout.activity_menu_login);

                    TextView txtUsername = (TextView) findViewById(R.id.login_menu_user_name);
                    txtUsername.setText(prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));

                    TextView txtLogout = (TextView) findViewById(R.id.btn_signout);
                    txtLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // implement logout funcionality
                            _wsclogingeneral.logout(LoginHomeActivity.this);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_login_phone);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Charile Taxi");
                            alertDialog.setMessage("1514");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:1514"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.login_menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:81717272"));
                                                startActivity(intent);
                                            } else {
                                                _wsc.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_login_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_login_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FeaturedDealsActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_login_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_login_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_login_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llFavorites = (LinearLayout) findViewById(R.id.menu_login_favorites);
                    llFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FavoriteActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_login_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_login_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(LoginHomeActivity.this, ContactUsActivity.class);
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

                WSCLogin _wsc = new WSCLogin();
                _wsc.getUserFavoritesFeaturedDeals(this, homeView6, prefs.getLong(Enums.UserId, 0) + "", "all_deals", "");

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

                final EditText editText_categoriesgo = (EditText) categoriesGoView.findViewById(R.id.filterscreen_search);
                editText_categoriesgo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsclogingeneral.getUserFavoritesFeaturedDeals(LoginHomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), prefs.getLong(Enums.UserId, 0) + "", "filter_categories_go",editText_categoriesgo.getText().toString());
                        }
                        return false;
                    }
                });

                WSCLogin _wsc7 = new WSCLogin();
                _wsc7.getUserFavoritesFeaturedDeals(this, categoriesGoView, prefs.getLong(Enums.UserId, 0) + "", "categories_go", "");

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
                final WSCLogin _wsclogin8 = new WSCLogin();
                _wsclogin8.getUserFavoritesFeaturedDeals(this, filterByLocationView, prefs.getLong(Enums.UserId, 0) + "", "filter_by_location", "");

                final EditText editText = (EditText) filterByLocationView.findViewById(R.id.filterscreen_search);
                editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsclogingeneral.getUserFavoritesFeaturedDeals(LoginHomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), prefs.getLong(Enums.UserId, 0) + "","filterscreen_location" ,editText.getText().toString());
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
                final WSCLogin _wsclogin9 = new WSCLogin();
                _wsclogin9.getUserFavoritesFeaturedDeals(this, filterByCategoriesView, prefs.getLong(Enums.UserId, 0) + "", "filter_by_categories", "");

                final EditText editText_categories = (EditText) filterByCategoriesView.findViewById(R.id.filterscreen_search);
                editText_categories.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsclogingeneral.getUserFavoritesFeaturedDeals(LoginHomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), prefs.getLong(Enums.UserId, 0) + "", "filterscreen_categories",editText_categories.getText().toString());
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

                break;
            case 10:
                final ViewGroup nearbylistView = (ViewGroup) inflater.inflate(R.layout.activity_nearby_listing, null);
                mainViewGroup.addView(nearbylistView);

                btnFilter = (LinearLayout) nearbylistView.findViewById(R.id.nearby_listing_filter);
                btnFilter.setOnClickListener(homeClickListener);

                RelativeLayout rlViewMap = (RelativeLayout) nearbylistView.findViewById(R.id.nearby_view_map);
                rlViewMap.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, LoginHomeActivity.class);
                        intent.putExtra("SELECTED_INDEX_ID", "1");
                        startActivity(intent);
                    }
                });

                TextView txtViewMap = (TextView) nearbylistView.findViewById(R.id.view_map);
                txtViewMap.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(context, LoginHomeActivity.class);
                        intent.putExtra("SELECTED_INDEX_ID", "1");
                        startActivity(intent);
                    }
                });

                final WSC _wsc10 = new WSC();

                if (mNav10 != null){
                    mNav10 = null;
                }
                if (mNav10 == null){
                    mNav10 = new SimpleSideDrawer(this);
                    mNav10.setLeftBehindContentView(R.layout.activity_menu_login);

                    TextView txtUsername = (TextView) findViewById(R.id.login_menu_user_name);
                    txtUsername.setText(prefs.getString(Enums.UserFirstName, "") + " " + prefs.getString(Enums.UserLastName, ""));

                    TextView txtLogout = (TextView) findViewById(R.id.btn_signout);
                    txtLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // implement logout funcionality
                            _wsclogingeneral.logout(LoginHomeActivity.this);
                        }
                    });

                    RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_login_phone);
                    btnCall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Charile Taxi");
                            alertDialog.setMessage("1514");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:1514"));
                                                startActivity(intent);
                                            } else {
                                                _wsc10.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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
                    RelativeLayout btnCallHotLine = (RelativeLayout) findViewById(R.id.login_menu_hotline);
                    btnCallHotLine.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog alertDialog = new AlertDialog.Builder(LoginHomeActivity.this).create();
                            alertDialog.setTitle("Call Hotline");
                            alertDialog.setMessage("81 717272");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            int result = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
                                            if (result == PackageManager.PERMISSION_GRANTED){
                                                Intent intent = new Intent(Intent.ACTION_CALL);
                                                intent.setData(Uri.parse("tel:81717272"));
                                                startActivity(intent);
                                            } else {
                                                _wsc10.requestPermission(LoginHomeActivity.this);
                                                int result_2 = ContextCompat.checkSelfPermission(LoginHomeActivity.this, Manifest.permission.CALL_PHONE);
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


                    LinearLayout llAboutPrivileb = (LinearLayout) findViewById(R.id.menu_login_aboutprivileb);
                    llAboutPrivileb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "5");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llAllDeals = (LinearLayout) findViewById(R.id.menu_login_alldeals);
                    llAllDeals.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FeaturedDealsActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llNearbyMe = (LinearLayout) findViewById(R.id.menu_login_nearbyme);
                    llNearbyMe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "1");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llPrivilebCard = (LinearLayout) findViewById(R.id.menu_login_privilebcard);
                    llPrivilebCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "2");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCategories = (LinearLayout) findViewById(R.id.menu_login_categories);
                    llCategories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LoginHomeActivity.class);
                            intent.putExtra("SELECTED_INDEX_ID", "3");
                            startActivity(intent);
                        }
                    });

                    LinearLayout llFavorites = (LinearLayout) findViewById(R.id.menu_login_favorites);
                    llFavorites.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, FavoriteActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llCharities = (LinearLayout) findViewById(R.id.menu_login_charities);
                    llCharities.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, CharitiesActivity.class);
                            startActivity(intent);
                        }
                    });

                    LinearLayout llContactUs = (LinearLayout) findViewById(R.id.menu_login_contactus);
                    llContactUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // contact us
                            Intent intent = new Intent(LoginHomeActivity.this, ContactUsActivity.class);
                            startActivity(intent);
                        }
                    });

                }

                nearMenu = (ImageButton) nearbylistView.findViewById(R.id.nearby_listing_menu);
                nearMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // contact us
                       mNav10.toggleLeftDrawer();
                    }
                });

                progressDialogGlobal = new SpotsDialog(LoginHomeActivity.this, R.style.Custom);
                progressDialogGlobal.show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("avocadol", prefs.getString(Enums.CurrentLatitude, "") + " " + prefs.getString(Enums.CurrentLongitude, ""));
                        WSC _wsc10 = new WSC();
                        stopService(new Intent(LoginHomeActivity.this, LocationUpdateService.class));
                        _wsclogingeneral.getUserFavoritesNearbyMap(LoginHomeActivity.this, nearbylistView, prefs.getLong(Enums.UserId, 0) + "", prefs.getString(Enums.CurrentLatitude, ""), prefs.getString(Enums.CurrentLongitude, ""));
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
                                                             Intent intent2 = new Intent(context, LoginHomeActivity.class);
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
                                                               Intent intent2 = new Intent(context, LoginHomeActivity.class);
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
                                                          Intent intent2 = new Intent(context, LoginHomeActivity.class);
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
                                                            Intent intent2 = new Intent(context, LoginHomeActivity.class);
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
                // filter by both location and categories view
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
                _wsclogingeneral.getUserFavoritesFeaturedDeals(this, filterByLocationAndCategoriesView, prefs.getLong(Enums.UserId, 0) + "", "filter_by_location_and_categories", "");

                final EditText editText_both = (EditText) filterByLocationAndCategoriesView.findViewById(R.id.filterscreen_search);
                editText_both.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            _wsclogingeneral.getUserFavoritesFeaturedDeals(LoginHomeActivity.this, (ViewGroup) findViewById(R.id.filterscreen_main), prefs.getLong(Enums.UserId, 0) + "", "filterscreen_location_and_categories", editText_both.getText().toString());
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

                break;
            case 16:

            default:
                break;
        }
    }

    void showMenuContents(int index){
        switch (index) {
            case 0:
                Intent intent = new Intent(context, FeaturedDealsActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(context, LoginHomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "1");
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(context, LoginHomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(context, LoginHomeActivity.class);
                intent3.putExtra("SELECTED_INDEX_ID", "3");
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(context, FavoriteActivity.class);
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
                Intent intent2 = new Intent(context, LoginHomeActivity.class);
                intent2.putExtra("SELECTED_INDEX_ID", "5");
                startActivity(intent2);
                break;
            case 1:
                Intent intent1 = new Intent(context, LoginHomeActivity.class);
                intent1.putExtra("SELECTED_INDEX_ID", "2");
                startActivity(intent1);
                break;
            case 2:
                // benefits english
                Intent intent11 = new Intent(context, LoginHomeActivity.class);
                intent11.putExtra("SELECTED_INDEX_ID", "11");
                startActivity(intent11);
                break;
            case 3:
                Intent intent13 = new Intent(context, LoginHomeActivity.class);
                intent13.putExtra("SELECTED_INDEX_ID", "13");
                startActivity(intent13);
                break;
            case 4:
                Intent intent = new Intent(context, ContactUsActivity.class);
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
                    PrivilebApplication.mNav.toggleLeftDrawer();
                    //btnMenu.setImageResource(R.drawable.color_menu);
                    //Intent intent3 = new Intent(context, MenuActivity.class);
                    //startActivity(intent3);
                    break;
                case R.id.home_btn_featured:
                    Intent intent0 = new Intent(context, FeaturedDealsActivity.class);
                    startActivity(intent0);
                    break;
                case R.id.home_btn_alldeals:
                    Intent intent6 = new Intent(context, AllDealsActivity.class);
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
                    PrivilebApplication.mNav.toggleLeftDrawer();
                    break;
                case R.id.nearby_menu:
                    PrivilebApplication.mNav.toggleLeftDrawer();
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
                stopService(new Intent(LoginHomeActivity.this, LocationUpdateService.class));
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
                progressDialogGlobal = new SpotsDialog(LoginHomeActivity.this, R.style.Custom);
                progressDialogGlobal.show();
                SupportMapFragment supportMapFragment;
                if (Build.VERSION.SDK_INT < 21) {
                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_nearby);
                    supportMapFragment.getMapAsync(LoginHomeActivity.this);
                } else {
                    MapFragment mapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(R.id.map_nearby);
                    mapFragment.getMapAsync(LoginHomeActivity.this);
                }
            }
        }
    }

}
