package com.eventus.privileb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eventus.privileb.General.Enums;
import com.eventus.privileb.Util.LocationUpdateService;
import com.eventus.privileb.Util.WSC;
import com.eventus.privileb.Util.WSCLogin;
import com.navdrawer.SimpleSideDrawer;

import dmax.dialog.SpotsDialog;

public class NearbyListingActivity extends AppCompatActivity {

    private Context context = this;
    private SharedPreferences prefs;
    private WSCLogin _wsc;
    private LinearLayout btnFilter;
    private ImageButton btnMenu;
    EditText editText;
    private LinearLayout tabs[];
    private int selectTabIndex = 0;
    private ImageButton nearMenu;
    AlertDialog progressDialogGlobal;
    private SimpleSideDrawer mNav10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_listing);
        _wsc = new WSCLogin();
        prefs = this.getSharedPreferences(Enums.MyPREFERENCES, Context.MODE_PRIVATE);

        btnFilter = (LinearLayout) findViewById(R.id.nearby_listing_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearbyListingActivity.this, FilterActivity.class);
                startActivity(intent);
            }
        });

        RelativeLayout rlViewMap = (RelativeLayout) findViewById(R.id.nearby_view_map);
        rlViewMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(context, LoginHomeActivity.class);
                intent.putExtra("SELECTED_INDEX_ID", "1");
                startActivity(intent);
            }
        });

        TextView txtViewMap = (TextView) findViewById(R.id.view_map);
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
                    _wsc.logout(NearbyListingActivity.this);
                }
            });

            RelativeLayout btnCall = (RelativeLayout) findViewById(R.id.menu_login_phone);
            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(NearbyListingActivity.this).create();
                    alertDialog.setTitle("Call Charile Taxi");
                    alertDialog.setMessage("1514");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int result = ContextCompat.checkSelfPermission(NearbyListingActivity.this, android.Manifest.permission.CALL_PHONE);
                                    if (result == PackageManager.PERMISSION_GRANTED){
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:1514"));
                                        startActivity(intent);
                                    } else {
                                        _wsc10.requestPermission(NearbyListingActivity.this);
                                        int result_2 = ContextCompat.checkSelfPermission(NearbyListingActivity.this, android.Manifest.permission.CALL_PHONE);
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
                    final AlertDialog alertDialog = new AlertDialog.Builder(NearbyListingActivity.this).create();
                    alertDialog.setTitle("Call Hotline");
                    alertDialog.setMessage("81 717272");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Call",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int result = ContextCompat.checkSelfPermission(NearbyListingActivity.this, android.Manifest.permission.CALL_PHONE);
                                    if (result == PackageManager.PERMISSION_GRANTED){
                                        Intent intent = new Intent(Intent.ACTION_CALL);
                                        intent.setData(Uri.parse("tel:81717272"));
                                        startActivity(intent);
                                    } else {
                                        _wsc10.requestPermission(NearbyListingActivity.this);
                                        int result_2 = ContextCompat.checkSelfPermission(NearbyListingActivity.this, android.Manifest.permission.CALL_PHONE);
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
                    Intent intent = new Intent(context, LoginHomeActivity.class);
                    intent.putExtra("SELECTED_INDEX_ID", "4");
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
                    Intent intent = new Intent(NearbyListingActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                }
            });

        }

        nearMenu = (ImageButton) findViewById(R.id.nearby_listing_menu);
        nearMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // contact us
                mNav10.toggleLeftDrawer();
            }
        });

        /* progressDialogGlobal = new SpotsDialog(NearbyListingActivity.this, R.style.Custom);
        progressDialogGlobal.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("avocadol", prefs.getString(Enums.CurrentLatitude, "") + " " + prefs.getString(Enums.CurrentLongitude, ""));
                WSC _wsc10 = new WSC();
                stopService(new Intent(NearbyListingActivity.this, LocationUpdateService.class));
                _wsc.getUserFavoritesNearbyMap(NearbyListingActivity.this, (ViewGroup) findViewById(R.id.nearby_listing_main), prefs.getLong(Enums.UserId, 0) + "", prefs.getString(Enums.CurrentLatitude, ""), prefs.getString(Enums.CurrentLongitude, ""));
                progressDialogGlobal.dismiss();
            }
        }, 3000); */
    }

    @Override
    public void onResume()
    {
        super.onResume();
        startService(new Intent(this, LocationUpdateService.class));
        progressDialogGlobal = new SpotsDialog(NearbyListingActivity.this, R.style.Custom);
        progressDialogGlobal.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("avocadol", prefs.getString(Enums.CurrentLatitude, "") + " " + prefs.getString(Enums.CurrentLongitude, ""));
                WSC _wsc10 = new WSC();
                stopService(new Intent(NearbyListingActivity.this, LocationUpdateService.class));
                _wsc.getUserFavoritesNearbyMap(NearbyListingActivity.this, (ViewGroup) findViewById(R.id.nearby_listing_main), prefs.getLong(Enums.UserId, 0) + "", prefs.getString(Enums.CurrentLatitude, ""), prefs.getString(Enums.CurrentLongitude, ""));
                progressDialogGlobal.dismiss();
            }
        }, 3000);
    }
}
